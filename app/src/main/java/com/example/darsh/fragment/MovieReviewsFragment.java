package com.example.darsh.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.darsh.adapter.MovieReviewsListAdapter;
import com.example.darsh.helper.Constants;
import com.example.darsh.helper.StateHandler;
import com.example.darsh.model.MovieReview;
import com.example.darsh.model.MovieReviews;
import com.example.darsh.network.TmdbRestClient;
import com.example.darsh.popularmovies.R;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by darshan on 10/6/16.
 */
public class MovieReviewsFragment extends Fragment {
    private int id;
    private String title;
    private long voteCount;
    private double voteAverage;

    private MovieReviewsListAdapter adapter;
    private ArrayList<MovieReview> movieReviews;

    public MovieReviewsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt(Constants.BUNDLE_ID, 0);
            title = bundle.getString(Constants.BUNDLE_TITLE, "");
            voteCount = bundle.getLong(Constants.BUNDLE_VOTE_COUNT, 0);
            voteAverage = bundle.getDouble(Constants.BUNDLE_VOTE_AVERAGE, 0);
        }
        loadMovieReviews();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_reviews, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(title);

        TextView ratingText = (TextView) view.findViewById(R.id.text_view_rating);
        ratingText.setText(String.format(Locale.getDefault(), "%.2f", voteAverage));

        AppCompatRatingBar ratingBar = (AppCompatRatingBar) view.findViewById(R.id.rating_bar);
        ratingBar.setStepSize(2.0f);
        ratingBar.setRating((float) voteAverage);

        TextView voteCountText = (TextView) view.findViewById(R.id.text_view_view_count);
        voteCountText.setText(String.format(Locale.getDefault(), "%d", voteCount));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_reviews_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MovieReviewsListAdapter(getContext());
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new SpacingItemDecoration((int) getResources().getDimension(R.dimen.spacing_medium)));

        return view;
    }

    private void loadMovieReviews() {
        Call<MovieReviews> call = TmdbRestClient.getInstance()
                .getMovieReviewsImpl()
                .getMovieReviews(id, 1);
        Callback<MovieReviews> callback = new Callback<MovieReviews>() {
            private MovieReview temp;

            @Override
            public void onResponse(Call<MovieReviews> call, Response<MovieReviews> response) {
                if (!response.isSuccessful()) {
                    temp = StateHandler.handleMovieReviewState(getContext(), Constants.SERVER_ERROR);

                } else if (response.body().getMovieReviews().size() == 0){
                    temp = StateHandler.handleMovieReviewState(getContext(), Constants.NONE);

                } else {
                    Log.i(MovieReviewsFragment.class.getName(), "Has reviews");
                    movieReviews = response.body().getMovieReviews();
                }
                update();
            }

            @Override
            public void onFailure(Call<MovieReviews> call, Throwable t) {
                temp = StateHandler.handleMovieReviewState(getContext(), Constants.NETWORK_ERROR);
                update();
            }

            private void update() {
                if (temp != null) {
                    movieReviews = new ArrayList<>();
                    movieReviews.add(temp);
                    temp = null;
                }
                setupMovieReviewsView();
            }
        };
        call.enqueue(callback);
    }

    private void setupMovieReviewsView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setMovieReviews(movieReviews);
                adapter.notifyDataSetChanged();
            }
        }, 500);
    }

    private class SpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        public SpacingItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position < movieReviews.size()) {
                outRect.bottom = spacing;
            }
        }
    }
}
