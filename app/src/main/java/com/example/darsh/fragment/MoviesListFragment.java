package com.example.darsh.fragment;

import android.content.res.Configuration;
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
import com.example.darsh.popularmovies.R;
import com.example.darsh.view.EndlessScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darshan on 19/4/16.
 */
public class MoviesListFragment extends Fragment {
    private final String TAG = MoviesListFragment.class.getName();
    private final boolean DEBUG = true;

    private ProgressBar progressBar;

    private EndlessScrollRecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private MoviesListAdapter adapter;
    private ArrayList<Movie> movies;

    private int page;
    private int position;

    public MoviesListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList(Constants.MOVIES_LIST);
            page = Math.max(page, savedInstanceState.getInt(Constants.NEXT_PAGE));
            position = savedInstanceState.getInt(Constants.SCROLL_POSITION);

            if (movies != null) {
                if (DEBUG) Log.i(TAG, movies.get(0).getTitle() + " " + movies.get(1).getTitle());
                if (DEBUG) Log.i(TAG, "Page: " + page);
                if (DEBUG) Log.i(TAG, "Position: " + position);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        recyclerView = (EndlessScrollRecyclerView) view.findViewById(R.id.recycler_view_movies_list);
        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView() {
        int spanCount = getSpanCount();
        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpacingItemDecoration(
                (int) getResources().getDimension(R.dimen.padding_recycler_view)));
        recyclerView.setLoadingListener(new EndlessScrollRecyclerView.LoadingListener() {
            @Override
            public void onLoadMore() {
                page++;
                if (page <= 1000) {
                    loadMovies();
                    recyclerView.loadingComplete();
                }
            }
        });

        if (movies == null) {
            movies = new ArrayList<>();
            page = 1;
            position = 0;
        }
        adapter = new MoviesListAdapter(getActivity(), movies);
        recyclerView.setAdapter(adapter);
    }

    private int getSpanCount() {
        int spanCount = 2;
        if (getActivity() != null) {
            int orientation = getActivity().getResources()
                    .getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                spanCount = 3;
            }
        }
        return spanCount;
    }

    @Override
    public void onStart() {
        super.onStart();
        /*
        If movies list is of size zero, movies have to be
        fetched from tmdb. Hence display progress bar.
         */
        if (movies.size() == 0) {
            if (DEBUG) Log.i(TAG, "First time load");
            progressBar.setVisibility(View.VISIBLE);
            loadMovies();
        } else {
            recyclerView.scrollToPosition(position);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.MOVIES_LIST, movies);
        outState.putInt(Constants.NEXT_PAGE, page);
        outState.putInt(Constants.SCROLL_POSITION, ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
    }

    protected void loadMovies() {
    }

    protected void updateList(int numMovies, int numMoviesDownloaded) {
        if (page == 1) {
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            return;
        }
        adapter.notifyItemRangeInserted(numMovies, numMoviesDownloaded);
    }

    protected void addMovies(List<Movie> movies) {
        this.movies.addAll(movies);
    }

    protected int getNumMovies() {
        return movies.size();
    }

    protected int getPage() {
        return page;
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
