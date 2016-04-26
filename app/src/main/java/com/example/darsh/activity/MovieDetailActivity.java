package com.example.darsh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.darsh.popularmovies.R;

/**
 * Created by darshan on 14/4/16.
 */
public class MovieDetailActivity extends AppCompatActivity {
    private final String TAG = MovieDetailActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie_detail);

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            Log.d(TAG, "Title: " + title);
        }
    }

}
