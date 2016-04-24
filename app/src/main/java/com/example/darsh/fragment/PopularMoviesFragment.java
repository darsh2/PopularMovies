package com.example.darsh.fragment;

import com.example.darsh.model.Result;
import com.example.darsh.network.TmdbRestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by darshan on 24/4/16.
 */
public class PopularMoviesFragment extends MoviesListFragment {
    public PopularMoviesFragment() {}

    @Override
    protected void loadMovies() {
        int page = getPage();
        Call<Result> call = TmdbRestClient.getPopularMoviesImpl().getPopularMovies(page);
        Callback<Result> callback = new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                int numMovies = getNumMovies();
                int numMoviesDownloaded = response.body().getResults().size();
                addMovies(response.body().getResults());
                updateList(numMovies, numMoviesDownloaded);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        };
        call.enqueue(callback);
    }
}
