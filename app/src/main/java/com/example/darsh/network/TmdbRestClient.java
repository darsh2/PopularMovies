package com.example.darsh.network;

import com.example.darsh.popularmovies.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by darshan on 4/4/16.
 */
public class TmdbRestClient {
    private static String BASE_URL = "https://api.themoviedb.org/3/";

    private MoviesApi.PopularMovies popularMovies;
    private MoviesApi.TopRatedMovies topRatedMovies;

    private MoviesApi.MovieDetails movieDetails;
    private MoviesApi.GenreMovies genreMovies;
    private MoviesApi.MovieDetailVideos movieVideos;
    private MoviesApi.MovieDetailReviews movieReviews;
    private MoviesApi.SimilarMovies similarMovies;

    private MoviesApi.MovieBackdropImages movieBackdropImages;
    private MoviesApi.MovieCredits movieCredits;

    private Retrofit retrofit;

    private static TmdbRestClient instance = null;

    private TmdbRestClient() {
        initializeRetrofit();
    }

    public static TmdbRestClient getInstance() {
        if (instance == null) {
            instance = new TmdbRestClient();
        }
        return instance;
    }

    public MoviesApi.PopularMovies getPopularMoviesImpl() {
        if (popularMovies == null) {
            popularMovies = retrofit.create(MoviesApi.PopularMovies.class);
        }
        return popularMovies;
    }

    public MoviesApi.TopRatedMovies getTopRatedMoviesImpl() {
        if (topRatedMovies == null) {
            topRatedMovies = retrofit.create(MoviesApi.TopRatedMovies.class);
        }
        return topRatedMovies;
    }

    public MoviesApi.MovieDetails getMovieDetailsImpl() {
        if (movieDetails == null) {
            movieDetails = retrofit.create(MoviesApi.MovieDetails.class);
        }
        return movieDetails;
    }

    public MoviesApi.GenreMovies getGenreMoviesImpl() {
        if (genreMovies == null) {
            genreMovies = retrofit.create(MoviesApi.GenreMovies.class);
        }
        return genreMovies;
    }

    public MoviesApi.MovieDetailVideos getMovieVidoesImpl() {
        if (movieVideos == null) {
            movieVideos = retrofit.create(MoviesApi.MovieDetailVideos.class);
        }
        return movieVideos;
    }

    public MoviesApi.MovieDetailReviews getMovieReviewsImpl() {
        if (movieReviews == null) {
            movieReviews = retrofit.create(MoviesApi.MovieDetailReviews.class);
        }
        return movieReviews;
    }

    public MoviesApi.SimilarMovies getSimilarMoviesImpl() {
        if (similarMovies == null) {
            similarMovies = retrofit.create(MoviesApi.SimilarMovies.class);
        }
        return similarMovies;
    }

    public MoviesApi.MovieBackdropImages getMovieBackdropImagesImpl() {
        if (movieBackdropImages == null) {
            movieBackdropImages = retrofit.create(MoviesApi.MovieBackdropImages.class);
        }
        return movieBackdropImages;
    }

    public MoviesApi.MovieCredits getMovieCreditsImpl() {
        if (movieCredits == null) {
            movieCredits = retrofit.create(MoviesApi.MovieCredits.class);
        }
        return movieCredits;
    }

    /**
     * Helper function to add the api key parameter
     * to all requests that are made through {@link Retrofit}.
     * Sets the base URL of the requests and the
     * {@link GsonConverterFactory} for parsing the
     * JSON responses.
     */
    private void initializeRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url()
                                .newBuilder()
                                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                                .build();
                        Request.Builder builder = request.newBuilder()
                                .url(url)
                                .method(request.method(), request.body());
                        request = builder.build();
                        return chain.proceed(request);
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}
