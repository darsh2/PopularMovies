package com.example.darsh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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

    private ViewPager viewPager;

    private boolean isTablet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        isTablet = getResources().getBoolean(R.bool.is_tablet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name));
        }
        /*
        In the case of tablet, the other fragment has a different
        toolbar. Hence set as ActionBar only if it is a phone.
         */
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
        /*
        If savedInstanceState is not null, do not recreate the fragments.
        Use the references to the previously created ones.
         */
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*
        Fragment transactions are added to the backstack. If loading
        the fragments from a savedInstanceState, the fragments in the
        view R.id.container_movie_detail are recreated by Android. Hence
        there is no need of storing a reference to these fragments. On
        the other hand, the movie list fragments are not recreated
        probably because the viewpager is itself not persisted. Hence
        store references to these fragments to avoid reinitialising
        them when loading from a savedInstanceState.
         */
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
    }

    @Override
    public void onMovieClick(Movie movie) {
        if (!isTablet) {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(Constants.INTENT_EXTRA_MOVIE, movie);
            startActivity(intent);

        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.BUNDLE_MOVIE, movie);

            /*
            If tablet, the MovieDetailFragment will be loaded in the same
            activity in the view R.id.container_movie_detail. Using replace
            instead of add because internally it will work as
            replace(currentFragment).add(viewId, newFragment, null). This
            will prevent overlapping of fragments. However, the for the
            first time this fragment is loaded, it will be as
            replace(null).add(viewId, newFragment, null) as there are no
            fragments in that view.
             */
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_movie_detail, movieDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
