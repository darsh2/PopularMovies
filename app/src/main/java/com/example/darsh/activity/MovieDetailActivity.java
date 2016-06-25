package com.example.darsh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.darsh.fragment.AboutMovieFragment;
import com.example.darsh.fragment.MovieDetailFragment;
import com.example.darsh.fragment.MovieReviewsFragment;
import com.example.darsh.popularmovies.R;

import java.util.List;

/**
 * Created by darshan on 14/4/16.
 */
public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        /*
        If this activity is being recreated from a savedInstanceState,
        the fragments will still be in the FragmentManager. Do not
        recreate the fragment or it will lead to overlapping fragments
        in the same view.
         */
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = 0, l = fragments.size(); i < l; i++) {
                Fragment fragment = fragments.get(i);
                if (fragment instanceof MovieDetailFragment
                        || fragment instanceof AboutMovieFragment
                        || fragment instanceof MovieReviewsFragment) {
                    return;
                }
            }
        }

        /*
        Unlike in MovieDetailActivity's onMovieClick, using add(viewId, fragment)
        because this is only for the first MovieDetailFragment to loaded
        from this Activity. On clicking a movie found in similar movies
        in this fragment, the transactions use replace(viewId, fragment).
         */
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_movie_detail, movieDetailFragment)
                .commit();
    }
}
