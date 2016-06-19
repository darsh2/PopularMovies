package com.example.darsh.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.darsh.adapter.MoviesListAdapter;
import com.example.darsh.database.MovieContract;
import com.example.darsh.helper.Constants;
import com.example.darsh.model.Movie;
import com.example.darsh.popularmovies.R;
import com.example.darsh.view.EndlessScrollRecyclerView;

import java.util.ArrayList;

/**
 * Created by darshan on 18/6/16.
 */
public class FavoriteMoviesFragment extends MoviesListFragment {
    /*private ProgressBar progressBar;
    private EndlessScrollRecyclerView recyclerView;
    private MoviesListAdapter adapter;


    Used for communication between the UI and background
    threads.
    TODO:
    Experiment with other alternates like RxJava (reactive pattern)
    or a callback pattern like that of Retrofit.

    private Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_movies_list, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.LOADING: {
                        break;
                    }

                    case Constants.DONE: {
                        break;
                    }

                    default: {
                        super.handleMessage(msg);
                    }
                }
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }


    Override this method as there is no need of setting
    isLoading in RecyclerView. Favorite movies will be
    loaded at once instead of an endlessly scrolling list.

    private void loadMovies() {
    }

    private class FavoriteMoviesLoaderRunnable implements Runnable {
        @Override
        public void run() {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            sendMessage(Constants.LOADING);

            ArrayList<Movie> movies = new ArrayList<>();
            Cursor cursor = getContext().getContentResolver()
                    .query(
                            MovieContract.MovieEntry.CONTENT_URI,
                            new String[]{
                                    MovieContract.MovieColumns.MOVIE_ID,
                                    MovieContract.MovieColumns.MOVIE_TITLE,
                                    MovieContract.MovieColumns.MOVIE_RELEASE_DATE,
                                    MovieContract.MovieColumns.MOVIE_DURATION,
                                    MovieContract.MovieColumns.MOVIE_RATING,
                                    MovieContract.MovieColumns.MOVIE_POSTER_PATH,
                                    MovieContract.MovieColumns.MOVIE_BACKDROP_PATH
                            },
                            null, null, null
                    );
            if (cursor == null) {
                sendMessage(Constants.CURSOR_ERROR);
                return;
            }

            if (cursor.getCount() == 0) {
                sendMessage(Constants.NONE);
            } else {
                addMovies(movies);
                sendMessage(Constants.DONE);
            }
            cursor.close();
        }
    }

    private void sendMessage(int messageCode) {
        Message message = handler.obtainMessage();
        message.what = messageCode;
        message.sendToTarget();
    }*/

    @Override
    protected void loadMovies() {
        super.loadMovies();
        new AsyncDbTask().execute();
    }

    private class AsyncDbTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            ArrayList<Movie> movies = new ArrayList<>();
            Cursor cursor = getContext().getContentResolver()
                    .query(
                            MovieContract.MovieEntry.CONTENT_URI,
                            new String[]{
                                    MovieContract.MovieColumns.MOVIE_ID,
                                    MovieContract.MovieColumns.MOVIE_TITLE,
                                    MovieContract.MovieColumns.MOVIE_RELEASE_DATE,
                                    MovieContract.MovieColumns.MOVIE_DURATION,
                                    MovieContract.MovieColumns.MOVIE_RATING,
                                    MovieContract.MovieColumns.MOVIE_POSTER_PATH,
                                    MovieContract.MovieColumns.MOVIE_BACKDROP_PATH
                            },
                            null, null, null
                    );
            if (cursor == null) {
                return null;
            }

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_ID));
                String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_TITLE));
                String date = cursor.getString(cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_RELEASE_DATE));
                int duration = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_DURATION));
                double rating = cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_RATING));
                String poster = cursor.getString(cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_POSTER_PATH));
                String backdrop = cursor.getString(cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_BACKDROP_PATH));
                Movie movie = new Movie();
                movie.setId(id);
                movie.setTitle(title);
                movie.setReleaseDate(date);
                movie.setDuration(duration);
                movie.setVoteAverage(rating);
                movie.setPosterPath(poster);
                movie.setBackdropPath(backdrop);
                movies.add(movie);
            }
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            updateMovies(movies);
        }
    }
}
