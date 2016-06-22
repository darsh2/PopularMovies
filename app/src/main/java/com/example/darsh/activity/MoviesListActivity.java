package com.example.darsh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.darsh.adapter.FragmentTabsAdapter;
import com.example.darsh.adapter.MoviesListAdapter;
import com.example.darsh.fragment.FavoriteMoviesFragment;
import com.example.darsh.fragment.GenreMoviesFragment;
import com.example.darsh.fragment.MovieDetailFragment;
import com.example.darsh.fragment.PopularMoviesFragment;
import com.example.darsh.fragment.SimilarMoviesFragment;
import com.example.darsh.fragment.TopRatedMoviesFragment;
import com.example.darsh.helper.Constants;
import com.example.darsh.model.Movie;
import com.example.darsh.popularmovies.R;

/**
 * Created by darshan on 19/4/16.
 */
public class MoviesListActivity extends AppCompatActivity implements MoviesListAdapter.OnMovieClickListener {
    private final String TAG = MoviesListActivity.class.getName();

    private int type = Constants.MOVIES_GENERAL;

    private PopularMoviesFragment popularMoviesFragment;
    private TopRatedMoviesFragment topRatedMoviesFragment;
    private FavoriteMoviesFragment favoriteMoviesFragment;

    private SimilarMoviesFragment similarMoviesFragment;
    private int movieId;
    private String movieTitle;

    private GenreMoviesFragment genreMoviesFragment;
    private int genreId;
    private String genre;

    private TextView noneSelected;
    private MovieDetailFragment movieDetailFragment;

    private ViewPager viewPager;

    private boolean isTablet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        if (Constants.DEBUG) Log.i(TAG, "onCreate");

        isTablet = getResources().getBoolean(R.bool.is_tablet);
        if (isTablet) {
            noneSelected = (TextView) findViewById(R.id.text_view_none_selected);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name));
        }
        if (!isTablet) {
            setSupportActionBar(toolbar);
        }

        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra(Constants.INTENT_EXTRA_TYPE, Constants.MOVIES_GENERAL);
            if (type == Constants.MOVIES_SIMILAR) {
                movieId = intent.getIntExtra(Constants.BUNDLE_ID, 0);
                movieTitle = intent.getStringExtra(Constants.BUNDLE_TITLE);

            } else if (type == Constants.MOVIES_GENRE) {
                genreId = intent.getIntExtra(Constants.BUNDLE_GENRE_ID, 0);
                genre = intent.getStringExtra(Constants.BUNDLE_GENRE);
            }
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        initFragments(savedInstanceState);
        setupViewPager();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setupViewPager() {
        FragmentTabsAdapter adapter = new FragmentTabsAdapter(getSupportFragmentManager());
        switch (type) {
            case Constants.MOVIES_GENERAL: {
                adapter.addFragment(popularMoviesFragment, getString(R.string.popular));
                adapter.addFragment(topRatedMoviesFragment, getString(R.string.top_rated));
                adapter.addFragment(favoriteMoviesFragment, getString(R.string.favorites));
                viewPager.setOffscreenPageLimit(3);
                break;
            }

            case Constants.MOVIES_SIMILAR: {
                adapter.addFragment(similarMoviesFragment, getString(R.string.similar_to) + " " + movieTitle);
                break;
            }

            case Constants.MOVIES_GENRE: {
                adapter.addFragment(genreMoviesFragment, genre);
                break;
            }
        }
        viewPager.setAdapter(adapter);
    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            loadFromSavedInstanceState(savedInstanceState);
            return;
        }

        switch (type) {
            case Constants.MOVIES_GENERAL: {
                popularMoviesFragment = new PopularMoviesFragment();
                topRatedMoviesFragment = new TopRatedMoviesFragment();
                favoriteMoviesFragment = new FavoriteMoviesFragment();
                break;
            }

            case Constants.MOVIES_SIMILAR: {
                similarMoviesFragment = new SimilarMoviesFragment();
                similarMoviesFragment.setId(movieId);
                break;
            }

            case Constants.MOVIES_GENRE: {
                genreMoviesFragment = new GenreMoviesFragment();
                genreMoviesFragment.setId(genreId);
                break;
            }
        }
    }

    private void loadFromSavedInstanceState(Bundle savedInstanceState) {
        switch (type) {
            case Constants.MOVIES_GENERAL: {
                popularMoviesFragment = (PopularMoviesFragment) getSupportFragmentManager().getFragment(savedInstanceState, PopularMoviesFragment.TAG);
                topRatedMoviesFragment = (TopRatedMoviesFragment) getSupportFragmentManager().getFragment(savedInstanceState, TopRatedMoviesFragment.TAG);
                favoriteMoviesFragment = (FavoriteMoviesFragment) getSupportFragmentManager().getFragment(savedInstanceState, FavoriteMoviesFragment.TAG);
                break;
            }

            case Constants.MOVIES_SIMILAR: {
                similarMoviesFragment = (SimilarMoviesFragment) getSupportFragmentManager().getFragment(savedInstanceState, SimilarMoviesFragment.TAG);
                break;
            }

            case Constants.MOVIES_GENRE: {
                genreMoviesFragment = (GenreMoviesFragment) getSupportFragmentManager().getFragment(savedInstanceState, GenreMoviesFragment.TAG);
                break;
            }
        }

        /*
        Get movieDetailFragment from the FragmentManager. If
        it is null, no movie has been selected, hence show the
        textView. Else, make it invisible.
         */
        movieDetailFragment = (MovieDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, MovieDetailFragment.TAG);
        if (movieDetailFragment == null) {
            noneSelected.setVisibility(View.VISIBLE);
        } else {
            noneSelected.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onMovieClick(Movie movie) {
        if (!isTablet) {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(Constants.INTENT_EXTRA_MOVIE, movie);
            startActivity(intent);

        } else {
            noneSelected.setVisibility(View.INVISIBLE);

            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.BUNDLE_MOVIE, movie);

            movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_movie_detail, movieDetailFragment)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Constants.DEBUG) Log.i(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Constants.DEBUG) Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Constants.DEBUG) Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Constants.DEBUG) Log.i(TAG, "onSaveInstanceState");
        switch (type) {
            case Constants.MOVIES_GENERAL: {
                getSupportFragmentManager().putFragment(outState, PopularMoviesFragment.TAG, popularMoviesFragment);
                getSupportFragmentManager().putFragment(outState, TopRatedMoviesFragment.TAG, topRatedMoviesFragment);
                getSupportFragmentManager().putFragment(outState, FavoriteMoviesFragment.TAG, favoriteMoviesFragment);
                break;
            }

            case Constants.MOVIES_SIMILAR: {
                getSupportFragmentManager().putFragment(outState, SimilarMoviesFragment.TAG, similarMoviesFragment);
                break;
            }

            case Constants.MOVIES_GENRE: {
                getSupportFragmentManager().putFragment(outState, GenreMoviesFragment.TAG, genreMoviesFragment);
                break;
            }
        }

        if (isTablet && movieDetailFragment != null) {
            getSupportFragmentManager().putFragment(outState, MovieDetailFragment.TAG, movieDetailFragment);
        }
    }
}
