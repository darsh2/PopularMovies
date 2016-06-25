package com.example.darsh.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.example.darsh.activity.MoviesListActivity;
import com.example.darsh.adapter.GenresListAdapter;
import com.example.darsh.adapter.SimilarMoviesAdapter;
import com.example.darsh.adapter.VideosListAdapter;
import com.example.darsh.database.MovieContract;
import com.example.darsh.helper.Constants;
import com.example.darsh.helper.StateHandler;
import com.example.darsh.model.Genre;
import com.example.darsh.model.Movie;
import com.example.darsh.model.MovieReview;
import com.example.darsh.model.MovieReviews;
import com.example.darsh.model.MovieVideo;
import com.example.darsh.model.MovieVideos;
import com.example.darsh.model.MoviesList;
import com.example.darsh.network.TmdbRestClient;
import com.example.darsh.popularmovies.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by darshan on 14/4/16.
 */
public class MovieDetailFragment extends Fragment
        implements GenresListAdapter.OnGenreClickListener, VideosListAdapter.OnVideoClickListener, SimilarMoviesAdapter.OnMovieClickListener {
    public static final String TAG = MovieDetailFragment.class.getName();

    private Movie movie;

    private boolean isFavorite;
    private FloatingActionButton favoriteButton;

    /*
    List of views whose references are required to be updated
    once the network fetches data about them. View references are
    obtained beforehand for a performance gain.
    Movie duration, horizontally scrollable genres genresRecyclerView,
    tag line text are updated after a successful api query.
     */
    private TextView duration;
    private TextView rating;

    private GenresListAdapter genresListAdapter;
    private RecyclerView genresRecyclerView;

    private TextView tagLine;
    private TextView overview;
    private TextView readMore;

    private VideosListAdapter videosListAdapter;
    private RecyclerView videosRecyclerView;

    private SimilarMoviesAdapter similarMoviesAdapter;
    private RecyclerView similarMoviesRecyclerView;

    private TextView reviewAuthor;
    private TextView reviewContent;
    private TextView reviewReadAll;

    private final String BACKDROP_IMAGE_URL = "http://image.tmdb.org/t/p/w500";
    private final String POSTER_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    private final String TMDB_MOVIE_URL = "https://www.themoviedb.org/movie/";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            movie = intent.getParcelableExtra(Constants.INTENT_EXTRA_MOVIE);
        }

        if (getArguments() != null) {
            movie = getArguments().getParcelable(Constants.BUNDLE_MOVIE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable(Constants.BUNDLE_MOVIE);
        }

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        initToolbarAndFAB(view);
        initMovieDetail(view);
        initMovieImages(view);
        initMovieGenres(view);
        initAboutMovie(view);
        initMovieVideos(view);
        initSimilarMovies(view);
        initMovieReviews(view);

        return view;
    }

    private void initToolbarAndFAB(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(movie.getTitle());
        toolbar.inflateMenu(R.menu.menu_movie_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() != R.id.menu_item_share_movie) {
                    return false;
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String url;
                /*
                If the movie has no videos, share it's
                tmdb web page. Else share it's first trailer
                video.
                 */
                if (movie.getMovieVideos() == null ||
                        movie.getMovieVideos().size() == 0) {
                    url = TMDB_MOVIE_URL + movie.getId();
                } else {
                    url = Constants.URI_YOUTUBE_BROWSER + movie.getMovieVideos().get(0).getKey();
                }
                intent.putExtra(Intent.EXTRA_TEXT, url);
                startActivity(Intent.createChooser(intent, getString(R.string.share_via)));
                return true;
            }
        });

        favoriteButton = (FloatingActionButton) view.findViewById(R.id.button_favorite);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FavoriteTogglerTask().execute();
            }
        });
        /*
        Determine if this movie is added to favorites.
         */
        new FavoriteCheckerTask().execute();
    }

    private void initMovieDetail(View view) {
        TextView title = (TextView) view.findViewById(R.id.text_view_title);
        title.setText(movie.getTitle());

        TextView releaseDate = (TextView) view.findViewById(R.id.text_view_release_date);
        releaseDate.setText(movie.getReleaseDate());

        rating = (TextView) view.findViewById(R.id.text_view_rating);
        String voteAverage = Double.toString(movie.getVoteAverage());
        rating.setText(voteAverage);

        duration = (TextView) view.findViewById(R.id.text_view_duration);
    }

    private void initMovieImages(View view) {
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
    }

    private void initMovieGenres(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        genresListAdapter = new GenresListAdapter(MovieDetailFragment.this);
        genresRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_genres_list);
        genresRecyclerView.setLayoutManager(linearLayoutManager);
        genresRecyclerView.setAdapter(genresListAdapter);
        genresRecyclerView.addItemDecoration(new SpacingItemDecoration(
                (int) getResources().getDimension(R.dimen.spacing_genre)));
    }

    private void initAboutMovie(View view) {
        overview = (TextView) view.findViewById(R.id.text_view_overview);
        overview.setText(movie.getOverview());
        tagLine = (TextView) view.findViewById(R.id.text_view_tag_line);
        readMore = (TextView) view.findViewById(R.id.text_view_read_more);
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.BUNDLE_ID, movie.getId());
                bundle.putString(Constants.BUNDLE_TITLE, movie.getTitle());
                bundle.putString(Constants.BUNDLE_TAG_LINE, movie.getTagLine());
                bundle.putString(Constants.BUNDLE_OVERVIEW, movie.getOverview());

                AboutMovieFragment aboutMovieFragment = new AboutMovieFragment();
                aboutMovieFragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container_movie_detail, aboutMovieFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        readMore.setVisibility(View.INVISIBLE);
    }

    private void initMovieVideos(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        videosListAdapter = new VideosListAdapter(MovieDetailFragment.this);
        videosRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_videos_list);
        videosRecyclerView.setLayoutManager(linearLayoutManager);
        videosRecyclerView.setAdapter(videosListAdapter);
        videosRecyclerView.addItemDecoration(new SpacingItemDecoration((int) getResources().getDimension(R.dimen.spacing_genre)));
    }

    private void initSimilarMovies(View view) {
        TextView textView = (TextView) view.findViewById(R.id.text_view_see_more);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoviesListActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_TYPE, Constants.MOVIES_SIMILAR);
                intent.putExtra(Constants.BUNDLE_ID, movie.getId());
                intent.putExtra(Constants.BUNDLE_TITLE, movie.getTitle());
                startActivity(intent);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        similarMoviesAdapter = new SimilarMoviesAdapter(MovieDetailFragment.this);
        similarMoviesRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_similar_movies_list);
        similarMoviesRecyclerView.setLayoutManager(linearLayoutManager);
        similarMoviesRecyclerView.setAdapter(similarMoviesAdapter);
        similarMoviesRecyclerView.addItemDecoration(new SpacingItemDecoration((int) getResources().getDimension(R.dimen.spacing_small)));
    }

    private void initMovieReviews(View view) {
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
        loadSimilarMovies();
        loadMovieReviews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
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
                    /*
                    If movie is loaded from Favorites, it's overview
                    text is an empty string. Hence reset overview text.
                     */
                    movie.setOverview(response.body().getOverview());
                    /*
                    Reset both vote count and vote average as they
                    may be outdated.
                     */
                    movie.setVoteCount(response.body().getVoteCount());
                    movie.setVoteAverage(response.body().getVoteAverage());
                }
                update();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                movie.setTagLine(StateHandler.handleMovieDetailState(getContext(), Constants.NETWORK_ERROR));
                update();
            }

            /**
             * Seemingly redundant to have this method which just
             * passes on call to {@link MovieDetailFragment#updateUI}.
             * However can allow for greater customization depending
             * on whether it is called from {@link Callback#onResponse}
             * or {@link Callback#onFailure}. Future scope to possibly
             * modify UI. Check {@link MovieDetailFragment#loadMovieVideos()}
             * and {@link MovieDetailFragment#loadMovieReviews()} for
             * use case of this style.
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

    @Override
    public void onGenreClick(Genre genre) {
        Intent intent = new Intent(getActivity(), MoviesListActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_TYPE, Constants.MOVIES_GENRE);
        intent.putExtra(Constants.BUNDLE_GENRE_ID, genre.getId());
        intent.putExtra(Constants.BUNDLE_GENRE, genre.getName());
        startActivity(intent);
    }

    private void setupAboutMovieView() {
        String runtime = Integer.toString(movie.getDuration()) + " minutes";
        duration.setText(runtime);

        rating.setText(String.valueOf(movie.getVoteAverage()));
        if (movie.getTagLine() != null && movie.getTagLine().length() > 0) {
            tagLine.setText(movie.getTagLine());
        }
        overview.setText(movie.getOverview());
        if (overview != null || overview.length() > 0) {
            readMore.setVisibility(View.VISIBLE);
        }
    }

    private void loadMovieVideos() {
        if (movie.getMovieVideos() != null &&
                movie.getMovieVideos().size() > 0) {
            /*
            Movies are already loaded. In this case, just
            update the view. This happens when the activity
            is recreated on configuration change in which
            case the movie videos are loaded from the
            savedInstanceState.
             */
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

        /*
        Play video using YouTube app if it exists,
        else default to browser.
        Taken from: http://stackoverflow.com/a/12439378/3946664
         */
        /*
        try {
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Constants.URI_YOUTUBE_APP + movieVideo.getKey()));
        } catch (android.content.ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Constants.URI_YOUTUBE_BROWSER + movieVideo.getKey()));
        }
        The above code for opening youtube video does not work on emulators.
        So defaulting to opening using browser url.
         */
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(Constants.URI_YOUTUBE_BROWSER + movieVideo.getKey()));
        startActivity(intent);
    }

    private void setupMovieVideos() {
        videosListAdapter.setVideos(movie.getMovieVideos());
        videosListAdapter.notifyDataSetChanged();
        videosRecyclerView.setHasFixedSize(true);
    }

    private void loadSimilarMovies() {
        if (movie.getSimilarMovies() != null &&
                movie.getMovieVideos().size() > 0) {
            setupSimilarMovies();
            return;
        }
        Call<MoviesList> call = TmdbRestClient.getInstance()
                .getSimilarMoviesImpl()
                .getSimilarMovies(movie.getId(), 1);
        Callback<MoviesList> callback = new Callback<MoviesList>() {
            private Movie movieError;

            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (!response.isSuccessful()) {
                    movieError = StateHandler.handleSimilarMovieState(getContext(), Constants.SERVER_ERROR);
                } else if (response.body().getMovies().size() == 0) {
                    movieError = StateHandler.handleSimilarMovieState(getContext(), Constants.NONE);
                } else {
                    movie.setSimilarMovies(response.body().getMovies());
                }
                update();
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                movieError = StateHandler.handleSimilarMovieState(getContext(), Constants.NETWORK_ERROR);
                update();
            }

            private void update() {
                if (movieError != null) {
                    ArrayList<Movie> temp = new ArrayList<>();
                    temp.add(movieError);
                    movieError = null;

                    movie.setSimilarMovies(temp);
                }
                setupSimilarMovies();
            }
        };
        call.enqueue(callback);
    }

    @Override
    public void onMovieClick(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BUNDLE_MOVIE, movie);

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_movie_detail, movieDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void setupSimilarMovies() {
        similarMoviesAdapter.setSimilarMovies(movie.getSimilarMovies());
        similarMoviesAdapter.notifyDataSetChanged();
        similarMoviesRecyclerView.setHasFixedSize(true);
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

    private class FavoriteCheckerTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            Cursor cursor = getContext().getContentResolver()
                    .query(
                            MovieContract.MovieEntry.CONTENT_URI,
                            new String[]{MovieContract.MovieColumns.MOVIE_ID },
                            MovieContract.MovieColumns.MOVIE_ID + " = ?",
                            new String[]{ String.valueOf(movie.getId()) },
                            null
                    );
            boolean isExists = cursor != null && cursor.getCount() == 1;
            if (cursor != null) {
                cursor.close();
            }
            return isExists;
        }

        @Override
        protected void onPostExecute(Boolean isExists) {
            super.onPostExecute(isExists);
            if (isExists) {
                favoriteButton.setImageResource(R.drawable.ic_star_white_24dp);
            } else {
                favoriteButton.setImageResource(R.drawable.ic_star_border_white_24dp);
            }
            isFavorite = isExists;
        }
    }

    private class FavoriteTogglerTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            boolean isSuccessful;
            /*
            If movie is already among favorites, remove it.
            Else add.
             */
            if (isFavorite) {
                isSuccessful = getContext().getContentResolver()
                        .delete(
                                MovieContract.MovieEntry.CONTENT_URI,
                                MovieContract.MovieColumns.MOVIE_ID + " = ?",
                                new String[]{ String.valueOf(movie.getId()) }
                        ) == 1;
            } else {
                isSuccessful = getContext().getContentResolver()
                        .insert(MovieContract.MovieEntry.CONTENT_URI, getContentValues()) != null;
            }
            return isSuccessful;
        }

        @Override
        protected void onPostExecute(Boolean isSuccessful) {
            super.onPostExecute(isSuccessful);
            if (!isSuccessful) {
                showToast(getString(R.string.op_failed));
                return;
            }
            isFavorite = !isFavorite;
            if (isFavorite) {
                favoriteButton.setImageResource(R.drawable.ic_star_white_24dp);
                showToast(getString(R.string.movie_added));
            } else {
                favoriteButton.setImageResource(R.drawable.ic_star_border_white_24dp);
                showToast(getString(R.string.movie_removed));
            }
        }

        private ContentValues getContentValues() {
            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieColumns.MOVIE_ID, movie.getId());
            values.put(MovieContract.MovieColumns.MOVIE_TITLE, movie.getTitle());
            values.put(MovieContract.MovieColumns.MOVIE_RELEASE_DATE, movie.getReleaseDate());
            values.put(MovieContract.MovieColumns.MOVIE_DURATION, movie.getDuration());
            values.put(MovieContract.MovieColumns.MOVIE_RATING, movie.getVoteAverage());
            values.put(MovieContract.MovieColumns.MOVIE_POSTER_PATH, movie.getPosterPath());
            values.put(MovieContract.MovieColumns.MOVIE_BACKDROP_PATH, movie.getBackdropPath());
            return values;
        }
    }

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
