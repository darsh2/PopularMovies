package com.example.darsh.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.example.darsh.adapter.GenresListAdapter;
import com.example.darsh.adapter.VideosListAdapter;
import com.example.darsh.helper.Constants;
import com.example.darsh.helper.StateHandler;
import com.example.darsh.model.Movie;
import com.example.darsh.model.MovieReview;
import com.example.darsh.model.MovieReviews;
import com.example.darsh.model.MovieVideo;
import com.example.darsh.model.MovieVideos;
import com.example.darsh.network.TmdbRestClient;
import com.example.darsh.popularmovies.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by darshan on 14/4/16.
 */
public class MovieDetailFragment extends Fragment implements VideosListAdapter.OnVideoClickListener {
    private Movie movie;

    /*
    List of views whose references are required to be updated
    once the network fetches data about them. View references are
    obtained beforehand for a performance gain.
    Movie duration, horizontally scrollable genres genresRecyclerView,
    tag line text are updated after a successful api query.
     */
    private TextView duration;

    private GenresListAdapter genresListAdapter;
    private RecyclerView genresRecyclerView;

    /*
    Reference to the overview text is stored as absence of tag line
    would leave unused space below the list of movie genres.
    In such a scenario, remove margins of overview text.
     */
    private TextView tagLine;
    private TextView overview;

    private VideosListAdapter videosListAdapter;
    private RecyclerView videosRecyclerView;

    private TextView reviewAuthor;
    private TextView reviewContent;
    private TextView reviewReadAll;

    private final String BACKDROP_IMAGE_URL = "http://image.tmdb.org/t/p/w500";
    private final String POSTER_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            movie = intent.getParcelableExtra(Constants.INTENT_EXTRA_MOVIE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable(Constants.BUNDLE_MOVIE);
        }

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(movie.getTitle());

        /*
        Setting up movie detail view
         */
        TextView title = (TextView) view.findViewById(R.id.text_view_title);
        title.setText(movie.getTitle());

        TextView releaseDate = (TextView) view.findViewById(R.id.text_view_release_date);
        releaseDate.setText(movie.getReleaseDate());

        TextView rating = (TextView) view.findViewById(R.id.text_view_rating);
        String voteAverage = Double.toString(movie.getVoteAverage());
        rating.setText(voteAverage);

        duration = (TextView) view.findViewById(R.id.text_view_duration);

        /*
        Initialise layout of genres RecyclerView beforehand.
        Obtain references to duration and tag line TextViews.
         */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        genresListAdapter = new GenresListAdapter();
        genresRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_genres_list);
        genresRecyclerView.setLayoutManager(linearLayoutManager);
        genresRecyclerView.setAdapter(genresListAdapter);
        genresRecyclerView.addItemDecoration(new SpacingItemDecoration(
                (int) getResources().getDimension(R.dimen.spacing_genre)));

        overview = (TextView) view.findViewById(R.id.text_view_overview);
        overview.setText(movie.getOverview());
        tagLine = (TextView) view.findViewById(R.id.text_view_tag_line);

        /*
        Load backdrop and poster images.
         */
        ImageView backdropImage = (ImageView) view.findViewById(R.id.image_view_backdrop);
        ImageView posterImage = (ImageView) view.findViewById(R.id.image_view_poster);

        Glide.with(view.getContext())
                .load(BACKDROP_IMAGE_URL + movie.getBackdropPath())
                .asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .placeholder(R.drawable.image_placeholder)
                .into(backdropImage);

        Glide.with(view.getContext())
                .load(POSTER_IMAGE_URL + movie.getPosterPath())
                .placeholder(R.drawable.image_placeholder)
                .into(posterImage);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        videosListAdapter = new VideosListAdapter(MovieDetailFragment.this);
        videosRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_videos_list);
        videosRecyclerView.setLayoutManager(linearLayoutManager);
        videosRecyclerView.setAdapter(videosListAdapter);
        videosRecyclerView.addItemDecoration(new SpacingItemDecoration((int) getResources().getDimension(R.dimen.spacing_genre)));

        reviewAuthor = (TextView) view.findViewById(R.id.text_view_review_author);
        reviewContent = (TextView) view.findViewById(R.id.text_view_review_content);
        reviewReadAll = (TextView) view.findViewById(R.id.text_view_review_read_all);
        reviewReadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Use bundle as opposed to a non default constructor
                to pass data to a fragment. This is because if at
                some point Android decides to recreate the fragment,
                it will call it's default no argument constructor.
                Read more: http://stackoverflow.com/questions/9245408/best-practice-for-instantiating-a-new-android-fragment/9245510#9245510
                 */
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.BUNDLE_ID, movie.getId());
                bundle.putString(Constants.BUNDLE_TITLE, movie.getTitle());
                bundle.putLong(Constants.BUNDLE_VOTE_COUNT, movie.getVoteCount());
                bundle.putDouble(Constants.BUNDLE_VOTE_AVERAGE, movie.getVoteAverage());

                MovieReviewsFragment movieReviewsFragment = new MovieReviewsFragment();
                movieReviewsFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_movie_detail, movieReviewsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
        On transitioning from this fragment to MovieReviewsFragment
        and back, the fragment is recreated and onCreate is not
        called. Hence load genres, trailers and reviews here.
         */
        loadMovieDetails();
        loadMovieVideos();
        loadMovieReviews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*
        Save the current state of the fragment by saving
        the Movie object to reduce the number of network
        calls.
         */
        outState.putParcelable(Constants.BUNDLE_MOVIE, movie);
    }

    private void loadMovieDetails() {
        if (movie.getTagLine() != null) {
            updateUI();
            return;
        }

        Call<Movie> call = TmdbRestClient.getInstance()
                .getMovieDetailsImpl()
                .getMovieDetails(movie.getId());
        Callback<Movie> callback = new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (!response.isSuccessful()) {
                    movie.setTagLine(StateHandler.handleMovieDetailState(getContext(), Constants.SERVER_ERROR));

                } else {
                    movie.setGenres(response.body().getGenres());
                    movie.setDuration(response.body().getDuration());
                    movie.setTagLine(response.body().getTagLine());
                }
                update();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                movie.setTagLine(StateHandler.handleMovieDetailState(getContext(), Constants.NETWORK_ERROR));
                update();
            }

            /*
            Seemingly redundant to have this method which just
            passes on call to updateUI. However can allow for
            greater customization depending on whether it is
            called from onResponse or onFailure. Future scope
            to possibly modify UI. Check loadMovieVideos() and
            loadMovieReviews() for use case of this style.
             */
            private void update() {
                updateUI();
            }
        };
        call.enqueue(callback);
    }

    private void updateUI() {
        setupGenresList();
        setupAboutMovieView();
    }

    private void setupGenresList() {
        genresListAdapter.setGenres(movie.getGenres());
        genresListAdapter.notifyDataSetChanged();
        genresRecyclerView.setHasFixedSize(true);
    }

    private void setupAboutMovieView() {
        String runtime = Integer.toString(movie.getDuration()) + " minutes";
        duration.setText(runtime);

        /*
        If the movie does not contain a tag line,
        remove margins of overview textview to get
        rid of unused space in layout.
         */
        if (movie.getTagLine() == null || movie.getTagLine().length() == 0) {
            tagLine.setVisibility(View.GONE);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            overview.setLayoutParams(layoutParams);
            overview.invalidate();
            return;
        }
        tagLine.setText(movie.getTagLine());
    }

    private void loadMovieVideos() {
        if (movie.getMovieVideos() != null &&
                movie.getMovieVideos().size() > 0) {
            setupMovieVideos();
            return;
        }

        Call<MovieVideos> call = TmdbRestClient.getInstance()
                .getMovieVidoesImpl()
                .getMovieVideos(movie.getId());
        Callback<MovieVideos> callback = new Callback<MovieVideos>() {
            private MovieVideo video;

            @Override
            public void onResponse(Call<MovieVideos> call, Response<MovieVideos> response) {
                if (!response.isSuccessful()) {
                    video = StateHandler.handleMovieVideoState(getContext(), Constants.SERVER_ERROR);

                } else if (response.body().getVideos().size() == 0) {
                    video = StateHandler.handleMovieVideoState(getContext(), Constants.NONE);

                } else {
                    movie.setMovieVideos(response.body().getVideos());
                }
                update();
            }

            @Override
            public void onFailure(Call<MovieVideos> call, Throwable t) {
                video = StateHandler.handleMovieVideoState(getContext(), Constants.NETWORK_ERROR);
                update();
            }

            private void update() {
                if (video != null) {
                    ArrayList<MovieVideo> videos = new ArrayList<>();
                    videos.add(video);
                    video = null;

                    movie.setMovieVideos(videos);
                }
                setupMovieVideos();
            }
        };
        call.enqueue(callback);
    }

    @Override
    public void onVideoClick(MovieVideo movieVideo) {
        /*
        Yet to check url to load for sites other than
        YouTube given video key. Currently supports
        only YouTube videos.
         */
        if (!movieVideo.getSite().equalsIgnoreCase(Constants.YOUTUBE)) {
            return;
        }

        Intent intent;
        /*
        Play video using YouTube app if it exists,
        else default to browser.
        Taken from: http://stackoverflow.com/a/12439378/3946664
         */
        try {
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Constants.URI_YOUTUBE_APP + movieVideo.getKey()));
        } catch (ActivityNotFoundException ex) {
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Constants.URI_YOUTUBE_BROWSER + movieVideo.getKey()));
        }
        startActivity(intent);
    }

    private void setupMovieVideos() {
        videosListAdapter.setVideos(movie.getMovieVideos());
        videosListAdapter.notifyDataSetChanged();
        videosRecyclerView.setHasFixedSize(true);
    }

    private void loadMovieReviews() {
        if (movie.getMovieReviews() != null &&
                movie.getMovieReviews().size() > 0) {
            setupMovieReviews();
            return;
        }

        Call<MovieReviews> call = TmdbRestClient.getInstance()
                .getMovieReviewsImpl()
                .getMovieReviews(movie.getId(), 1);
        Callback<MovieReviews> callback = new Callback<MovieReviews>() {
            private MovieReview review;

            @Override
            public void onResponse(Call<MovieReviews> call, Response<MovieReviews> response) {
                if (!response.isSuccessful()) {
                    review = StateHandler.handleMovieReviewState(getContext(), Constants.SERVER_ERROR);

                } else if (response.body().getMovieReviews().size() == 0) {
                    review = StateHandler.handleMovieReviewState(getContext(), Constants.NONE);

                } else {
                    review = response.body().getMovieReviews().get(0);
                }
                update();
            }

            @Override
            public void onFailure(Call<MovieReviews> call, Throwable t) {
                review = StateHandler.handleMovieReviewState(getContext(), Constants.NETWORK_ERROR);
                update();
            }

            private void update() {
                ArrayList<MovieReview> movieReviews = new ArrayList<>();
                movieReviews.add(review);
                review = null;

                movie.setMovieReviews(movieReviews);
                setupMovieReviews();
            }
        };
        call.enqueue(callback);
    }

    private void setupMovieReviews() {
        reviewAuthor.setText(movie.getMovieReviews().get(0).getAuthor());
        reviewContent.setText(movie.getMovieReviews().get(0).getContent());
        /*
        Hide read all reviews option if there
        are no reviews for the movie.
         */
        if (reviewContent.getText().toString()
                .compareTo(getString(R.string.no_reviews)) == 0) {
            reviewReadAll.setVisibility(View.INVISIBLE);
            reviewReadAll.setOnClickListener(null);
        }
    }

    private class SpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        public SpacingItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position == 0) {
                return;
            }
            outRect.left = spacing;
        }
    }
}
