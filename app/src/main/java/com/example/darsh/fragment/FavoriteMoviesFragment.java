package com.example.darsh.fragment;

import android.database.Cursor;
import android.os.AsyncTask;

import com.example.darsh.database.MovieContract;
import com.example.darsh.model.Movie;

import java.util.ArrayList;

/**
 * Created by darshan on 18/6/16.
 */
public class FavoriteMoviesFragment extends MoviesListFragment {
    public static final String TAG = FavoriteMoviesFragment.class.getName();

    public FavoriteMoviesFragment() {}

    @Override
    protected void loadMovies() {
        new AsyncDbTask().execute();
    }

    /**
     * Load movies from favorites.
     */
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
                String releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_RELEASE_DATE));
                int duration = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_DURATION));
                double rating = cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_RATING));
                String posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_POSTER_PATH));
                String backdropPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_BACKDROP_PATH));
                movies.add(new Movie(id, title, releaseDate, duration, rating, posterPath, backdropPath));
            }
            cursor.close();
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            addFavoriteMovies(movies);
        }
    }
}
