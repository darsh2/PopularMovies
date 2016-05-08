package com.example.darsh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.darsh.fragment.MovieDetailFragment;
import com.example.darsh.popularmovies.R;

import java.util.List;

/**
 * Created by darshan on 14/4/16.
 */
public class MovieDetailActivity extends AppCompatActivity {
    private final String TAG = MovieDetailActivity.class.getName();
    private final boolean DEBUG = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Log.i(TAG, "+onCreate");

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = 0, l = fragments.size(); i < l; i++) {
                if (fragments.get(i) instanceof MovieDetailFragment) {
                    Log.i(TAG, "--onCreate");
                    return;
                }
            }
        }

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_movie_detail, movieDetailFragment)
                .commit();
        Log.i(TAG, "-onCreate");
    }
}
