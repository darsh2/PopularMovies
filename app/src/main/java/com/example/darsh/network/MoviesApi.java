package com.example.darsh.network;

import com.example.darsh.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by darshan on 4/4/16.
 */
public class MoviesApi {
    public interface PopularMovies {
        @GET("movie/popular")
        Call<Result> getPopularMovies(@Query("page") Integer page);
    }

    public interface TopRatedMovies {
        @GET("movie/top_rated")
        Call<Result> getTopRatedMovies(@Query("page") Integer page);
    }
}
