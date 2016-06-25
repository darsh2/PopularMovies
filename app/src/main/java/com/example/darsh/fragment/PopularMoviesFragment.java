package com.example.darsh.fragment;

import com.example.darsh.helper.Constants;
import com.example.darsh.model.MoviesList;
import com.example.darsh.network.TmdbRestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by darshan on 24/4/16.
 */
public class PopularMoviesFragment extends MoviesListFragment {
    public static final String TAG = PopularMoviesFragment.class.getName();

    public PopularMoviesFragment() {}

    @Override
    protected void loadMovies() {
        super.loadMovies();
        Call<MoviesList> call = TmdbRestClient.getInstance()
                .getPopularMoviesImpl()
                .getPopularMovies(getPage());
        Callback<MoviesList> callback = new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (!response.isSuccessful()) {
                    retrievalError(Constants.SERVER_ERROR);
                    return;
                }
                setTotalPages(response.body().getTotalPages());
                addMovies(response.body().getMovies());
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                retrievalError(Constants.NETWORK_ERROR);
            }
        };
        call.enqueue(callback);
    }
}
