package com.example.darsh.fragment;

import com.example.darsh.model.MoviesList;
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
        Call<MoviesList> call = TmdbRestClient.getPopularMoviesImpl().getPopularMovies(page);
        Callback<MoviesList> callback = new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                int numMovies = getNumMovies();
                int numMoviesDownloaded = response.body().getMovies().size();
                addMovies(response.body().getMovies());
                updateList(numMovies, numMoviesDownloaded);
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {

            }
        };
        call.enqueue(callback);
    }
}
