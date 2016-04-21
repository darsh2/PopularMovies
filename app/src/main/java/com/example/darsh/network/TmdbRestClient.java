package com.example.darsh.network;

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

    private static MoviesApi.PopularMovies popularMovies;
    private static MoviesApi.TopRatedMovies topRatedMovies;

    private static Retrofit retrofit;

    public static MoviesApi.PopularMovies getPopularMoviesImpl() {
        if (retrofit == null) {
            initializeRetrofit();
        }
        if (popularMovies == null) {
            popularMovies = retrofit.create(MoviesApi.PopularMovies.class);
        }
        return popularMovies;
    }

    public static MoviesApi.TopRatedMovies getTopRatedMoviesImpl() {
        if (retrofit == null) {
            initializeRetrofit();
        }
        if (topRatedMovies == null) {
            topRatedMovies = retrofit.create(MoviesApi.TopRatedMovies.class);
        }
        return topRatedMovies;
    }

    private static void initializeRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url()
                                .newBuilder()
                                .addQueryParameter("api_key", "TMDB_API_KEY")
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
