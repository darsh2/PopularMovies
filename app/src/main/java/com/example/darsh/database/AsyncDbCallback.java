package com.example.darsh.database;

import com.example.darsh.model.Movie;

import java.util.ArrayList;

/**
 * Created by darshan on 19/6/16.
 */
public interface AsyncDbCallback {
    void onSucess(ArrayList<Movie> movies);

    void onFailure(Throwable throwable);
}
