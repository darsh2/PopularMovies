package com.example.darsh.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.example.darsh.adapter.GenresListAdapter;
import com.example.darsh.helper.Constants;
import com.example.darsh.model.Movie;
import com.example.darsh.network.TmdbRestClient;
import com.example.darsh.popularmovies.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by darshan on 14/4/16.
 */
public class MovieDetailFragment extends Fragment {
    private final String TAG = MovieDetailFragment.class.getName();

    private Movie movie;
    private View view;

    private final String BACKDROP_IMAGE_URL = "http://image.tmdb.org/t/p/w500";
    private final String POSTER_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            movie = intent.getParcelableExtra(Constants.INTENT_EXTRA_MOVIE);
            loadMovieDetails();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        this.view = view;
        setupView();
        return view;
    }

    private void setupView() {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(movie.getTitle());

        setupMovieImageViews();
        setupMovieDetailView();
    }

    private void setupMovieImageViews() {
        ImageView backdropImage = (ImageView) view.findViewById(R.id.image_view_backdrop);
        Glide.with(getActivity().getApplicationContext())
                .load(BACKDROP_IMAGE_URL + movie.getBackdropPath())
                .asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .placeholder(R.drawable.image_placeholder)
                .into(backdropImage);

        ImageView posterImage = (ImageView) view.findViewById(R.id.image_view_poster);
        Glide.with(view.getContext())
                .load(POSTER_IMAGE_URL + movie.getPosterPath())
                .placeholder(R.drawable.image_placeholder)
                .into(posterImage);
    }

    private void setupMovieDetailView() {
        TextView title = (TextView) view.findViewById(R.id.text_view_title);
        title.setText(movie.getTitle());

        TextView releaseDate = (TextView) view.findViewById(R.id.text_view_release_date);
        releaseDate.setText(movie.getReleaseDate());

        TextView rating = (TextView) view.findViewById(R.id.text_view_rating);
        String voteAverage = Double.toString(movie.getVoteAverage());
        rating.setText(voteAverage);
    }

    private void loadMovieDetails() {
        Call<Movie> call = TmdbRestClient.getInstance()
                .getMovieDetailsImpl()
                .getMovieDetails(movie.getId());
        Callback<Movie> callback = new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                movie.setGenres(response.body().getGenres());
                movie.setDuration(response.body().getDuration());
                movie.setTagLine(response.body().getTagLine());
                updateUI();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        };
        call.enqueue(callback);
    }

    private void updateUI() {
        setupGenresList();
        setupAboutMovieView();
    }

    private void setupGenresList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_genres_list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SpacingItemDecoration(
                (int) getResources().getDimension(R.dimen.spacing_genre)));

        GenresListAdapter adapter = new GenresListAdapter(movie.getGenres());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    private void setupAboutMovieView() {
        TextView duration = (TextView) view.findViewById(R.id.text_view_duration);
        String runtime = Integer.toString(movie.getDuration()) + " minutes";
        duration.setText(runtime);

        TextView tagLine = (TextView) view.findViewById(R.id.text_view_tag_line);
        tagLine.setText(movie.getTagLine());

        TextView overview = (TextView) view.findViewById(R.id.text_view_overview);
        overview.setText(movie.getOverview());
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
