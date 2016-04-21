package com.example.darsh.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.darsh.adapter.MoviesListAdapter;
import com.example.darsh.helper.Constants;
import com.example.darsh.model.Movie;
import com.example.darsh.model.Result;
import com.example.darsh.network.TmdbRestClient;
import com.example.darsh.popularmovies.R;
import com.example.darsh.view.EndlessScrollRecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by darshan on 19/4/16.
 */
public class MoviesListFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    private ProgressBar progressBar;

    private EndlessScrollRecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private MoviesListAdapter adapter;
    private ArrayList<Movie> movies;

    private int page = 1;

    public MoviesListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Constants.DEBUG) Log.i(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        recyclerView = (EndlessScrollRecyclerView) view.findViewById(R.id.recycler_view_movies_list);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpacingItemDecoration(
                (int) getResources().getDimension(R.dimen.padding_recycler_view)));
        recyclerView.setLoadingListener(new EndlessScrollRecyclerView.LoadingListener() {
            @Override
            public void onLoadMore() {
                if (Constants.DEBUG) Log.i(TAG, "onLoadMore");
                page++;
                if (Constants.DEBUG) Log.i(TAG, "Page Number: " + page);
                if (page <= 1000) {
                    loadPopularMovies();
                    recyclerView.loadingComplete();
                }
            }
        });

        if (movies == null) {
            movies = new ArrayList<>();
        }
        adapter = new MoviesListAdapter(getActivity().getApplicationContext(), movies);
        adapter.setSpanCount(gridLayoutManager.getSpanCount());
        recyclerView.setAdapter(adapter);

        loadPopularMovies();

        if (Constants.DEBUG) Log.i(TAG, "onCreateView");

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void loadPopularMovies() {
        Call<Result> call = TmdbRestClient.getPopularMoviesImpl().getPopularMovies(page);
        Callback<Result> callback = new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                int numMovies = movies.size();
                int numMoviesDownloaded = response.body().getResults().size();
                movies.addAll(response.body().getResults());
                if (page == 1) {
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    return;
                }
                adapter.notifyItemRangeInserted(numMovies, numMoviesDownloaded);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        };
        call.enqueue(callback);
    }

    private class SpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        public SpacingItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            /*
            Get equal spacing among views in GridLayoutManager
            Code taken from here: http://stackoverflow.com/a/30794046/3946664
             /
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(spacing, spacing, spacing, spacing);*/

            /*
            Initial implementation, seems computationally expensive
            due to modulus operation.
             */
            outRect.bottom = spacing;
            outRect.right = spacing;

            /*
            Apply top spacing only for first row of grid.
            The number of items in each row equals span count.
            Hence positions 0 to getSpanCount() - 1 are the
            first row items.
             */
            int childAdapterPosition = parent.getChildAdapterPosition(view);
            int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            if (childAdapterPosition < spanCount) {
                outRect.top = spacing;
            }
            /*
            Similarly, apply spacing on the left if current view
            is the first view on the left in the row.
             */
            if (childAdapterPosition % spanCount == 0) {
                outRect.left = spacing;
            }
        }
    }
}
