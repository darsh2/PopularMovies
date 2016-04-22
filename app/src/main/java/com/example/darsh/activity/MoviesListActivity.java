package com.example.darsh.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.darsh.fragment.MoviesListFragment;
import com.example.darsh.popularmovies.R;

/**
 * Created by darshan on 19/4/16.
 */
public class MoviesListActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();
    private final boolean DEBUG = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        if (DEBUG) Log.i(TAG, "onCreate");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MoviesListFragment(), MoviesListFragment.TAG)
                    .commit();
        }
    }
}
