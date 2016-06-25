package com.example.darsh.network;

import com.example.darsh.model.Credits;
import com.example.darsh.model.Movie;
import com.example.darsh.model.MovieImages;
import com.example.darsh.model.MovieReviews;
import com.example.darsh.model.MovieVideos;
import com.example.darsh.model.MoviesList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by darshan on 4/4/16.
 */

/**
 * Helper class that contains all the tmdb
 * API endpoints used for this app.
 */
public class MoviesApi {
    public interface PopularMovies {
        @GET("movie/popular")
        Call<MoviesList> getPopularMovies(@Query("page") Integer page);
    }

    public interface TopRatedMovies {
        @GET("movie/top_rated")
        Call<MoviesList> getTopRatedMovies(@Query("page") Integer page);
    }

    public interface MovieDetails {
        @GET("movie/{id}")
        Call<Movie> getMovieDetails(@Path("id") Integer id);
    }

    public interface GenreMovies {
        @GET("genre/{id}/movies")
        Call<MoviesList> getGenreMovies(@Path("id") Integer id, @Query("page") Integer page);
    }

    public interface MovieDetailVideos {
        @GET("movie/{id}/videos")
        Call<MovieVideos> getMovieVideos(@Path("id") Integer id);
    }

    public interface SimilarMovies {
        @GET("movie/{id}/similar")
        Call<MoviesList> getSimilarMovies(@Path("id") Integer id, @Query("page") Integer page);
    }

    public interface MovieDetailReviews {
        @GET("movie/{id}/reviews")
        Call<MovieReviews> getMovieReviews(@Path("id") Integer id, @Query("page") Integer page);
    }

    public interface MovieBackdropImages {
        @GET("movie/{id}/images")
        Call<MovieImages> getMovieBackdropImages(@Path("id") Integer id);
    }

    public interface MovieCredits {
        @GET("movie/{id}/credits")
        Call<Credits> getMovieCredits(@Path("id") Integer id);
    }
}
