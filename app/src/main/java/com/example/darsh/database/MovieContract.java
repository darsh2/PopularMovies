package com.example.darsh.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by darshan on 14/6/16.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.darsh";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public interface Tables {
        String MOVIE = "movie";
    }

    public interface MovieColumns {
        String MOVIE_ID = "_id";
        String MOVIE_TITLE = "title";
        String MOVIE_RELEASE_DATE = "date";
        String MOVIE_DURATION = "duration";
        String MOVIE_RATING = "rating";
        String MOVIE_BACKDROP_PATH = "backdrop";
        String MOVIE_POSTER_PATH = "poster";
    }

    public static final class MovieEntry implements BaseColumns, MovieColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    private MovieContract() {
        throw new AssertionError("No instances");
    }
}
