package com.example.darsh.fragment;

import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.darsh.database.MovieContract;
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
    private ProgressBar progressBar;

    private EndlessScrollRecyclerView recyclerView;
    private MoviesListAdapter adapter;
    private ArrayList<Movie> movies;

    private int totalPages = 1;
    private int page;
    private int position;

    private boolean isFavoritesFragment;
    private Handler handler;

    /**
     * Listens for changes in
     * {@link com.example.darsh.database.MovieContract.MovieEntry#BASE_CONTENT_URI}.
     * This is to immediately update the favorites movie list
     * when a new movie is added or an existing one is removed.
     */
    private ContentObserver contentObserver;

    public MoviesListFragment() {}

    private final String TAG = MoviesListFragment.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        Load list of movies, scroll position, and page number
        from savedInstanceState to restore state.
         */
        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList(Constants.MOVIES_LIST);
            page = Math.max(page, savedInstanceState.getInt(Constants.NEXT_PAGE));
            totalPages = savedInstanceState.getInt(Constants.TOTAL_PAGES);
            position = savedInstanceState.getInt(Constants.SCROLL_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Constants.DEBUG) Log.i(TAG, getClass().getName() + " onCreateView");
        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        recyclerView = (EndlessScrollRecyclerView) view.findViewById(R.id.recycler_view_movies_list);
        setUpRecyclerView();

        /*
        Determine the sub class type. If it is
        an instance of FavoriteMoviesFragment,
        set isFavoritesFragment to true.
         */
        Class<?> c = getClass();
        if (c.getName().endsWith("FavoriteMoviesFragment")) {
            isFavoritesFragment = true;
        }
        if (isFavoritesFragment) {
            registerObserver();
        }

        return view;
    }

    private void setUpRecyclerView() {
        int spanCount = getSpanCount();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpacingItemDecoration(
                (int) getResources().getDimension(R.dimen.spacing_movie)));
        if (!isFavoritesFragment) {
            recyclerView.setLoadingListener(new EndlessScrollRecyclerView.LoadingListener() {
                @Override
                public void onLoadMore() {
                    /*
                    If child count is 1, only the footerView is
                    shown. This implies that the first page is
                    yet to load. Hence increment page number
                    only if number of children is greater than 1.
                     */
                    if (recyclerView.getChildCount() <= 1) {
                        return;
                    }
                    page++;
                    if (page > totalPages) {
                        page = totalPages;
                        recyclerView.setState(Constants.NO_MORE);
                        return;
                    }
                    loadMovies();
                }
            });
        }

        /*
        If movies is null, then this is the first
        time the fragment is loaded. Initialise movies,
        page number and scroll position to default values.
         */
        if (movies == null) {
            movies = new ArrayList<>();
            page = 1;
            position = 0;
        }
        adapter = new MoviesListAdapter(getActivity(), movies);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(position);
    }

    /*
    Set number of columns in recyclerView
    depending on device orientation.
     */
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

    /**
     * Register a {@link ContentObserver} to listen for changes
     * in {@link MovieContract#BASE_CONTENT_URI}. On addition or
     * deletion of a movie from favorites, {@link #loadMovies()}
     * will be called to update movies list. This method is called
     * from {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * only if it is an instance of {@link FavoriteMoviesFragment}.
     */
    private void registerObserver() {
        handler = new Handler();
        contentObserver = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange) {
                loadMovies();
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                onChange(selfChange);
            }
        };
        getContext().getContentResolver()
                .registerContentObserver(MovieContract.BASE_CONTENT_URI, true, contentObserver);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (movies.size() == 0) {
            progressBar.setVisibility(View.VISIBLE);
            loadMovies();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isFavoritesFragment) {
            unregisterObserver();
        }
    }

    /**
     * Unregister the {@link ContentObserver} previously
     * registered by {@link #registerObserver()}. Free
     * up resources. This method is called from
     * {@link #onDestroyView()} only if it is an instance
     * of {@link FavoriteMoviesFragment}.
     */
    private void unregisterObserver() {
        getContext().getContentResolver()
                .unregisterContentObserver(contentObserver);
        contentObserver = null;
        handler = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.MOVIES_LIST, movies);
        outState.putInt(Constants.NEXT_PAGE, page);
        outState.putInt(Constants.TOTAL_PAGES, totalPages);
        outState.putInt(Constants.SCROLL_POSITION, ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
    }

    protected void loadMovies() {
        if (!isFavoritesFragment) {
            recyclerView.setIsLoading(true);

        } else {
            recyclerView.setState(Constants.LOADING_FAVORITES);
        }
    }

    protected void addMovies(List<Movie> movies) {
        int numMovies = this.movies.size();
        int numMoviesDownloaded = movies.size();
        this.movies.addAll(movies);
        updateList(numMovies, numMoviesDownloaded);
    }

    private void updateList(int numMovies, int numMoviesDownloaded) {
        recyclerView.setIsLoading(false);

        /*
        If it was the first page that was loaded,
        progressBar must be visible. Make it invisible.
         */
        if (page == 1) {
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            return;
        }
        recyclerView.loadingComplete();
        adapter.notifyItemRangeInserted(numMovies, numMoviesDownloaded);
    }

    protected void retrievalError(int code) {
        recyclerView.setIsLoading(false);

        if (page == 1) {
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        recyclerView.setState(code);
    }

    protected void addFavoriteMovies(List<Movie> movies) {
        this.movies.clear();
        if (movies == null) {
            recyclerView.setState(Constants.CURSOR_ERROR);
            return;
        }

        this.movies.addAll(movies);
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.INVISIBLE);
        if (movies.size() == 0) {
            recyclerView.setState(Constants.NONE);
        } else {
            recyclerView.setState(Constants.DONE);
        }
    }

    protected int getPage() {
        return page;
    }

    protected void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    private class SpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        public SpacingItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
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
