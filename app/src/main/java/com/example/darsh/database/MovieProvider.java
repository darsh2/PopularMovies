package com.example.darsh.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by darshan on 14/6/16.
 */
public class MovieProvider extends ContentProvider {
    private static final int MOVIE = 1001;
    private static final int MOVIE_WITH_ID = 1002;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private MovieDbHelper movieDbHelper;

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase sqLiteDatabase = movieDbHelper.getReadableDatabase();
        final int match = uriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            case MOVIE: {
                cursor = sqLiteDatabase.query(
                        MovieContract.Tables.MOVIE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case MOVIE_WITH_ID: {
                cursor = sqLiteDatabase.query(
                        MovieContract.Tables.MOVIE,
                        projection,
                        MovieContract.MovieColumns.MOVIE_ID + " = ?",
                        new String[]{ String.valueOf(ContentUris.parseId(uri)) },
                        null,
                        null,
                        sortOrder);
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIE: {
                return MovieContract.MovieEntry.CONTENT_TYPE;
            }

            case MOVIE_WITH_ID: {
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase sqLiteDatabase = movieDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        switch (match) {
            case MOVIE: {
                long id = sqLiteDatabase.insert(MovieContract.Tables.MOVIE, null, values);
                if (id == -1) {
                    sqLiteDatabase.close();
                    throw new SQLException("Failed to insert row into " + uri);
                }
                Uri returnUri = MovieContract.MovieEntry.buildMovieUri(id);
                notifyChange(uri);
                /*
                Release reference to the database by closing it.
                Avoid memory leaks.
                 */
                sqLiteDatabase.close();
                return returnUri;
            }

            default: {
                sqLiteDatabase.close();
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase sqLiteDatabase = movieDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;

        /*
        Makes delete all rows to return the number of rows deleted.
        Read here: https://github.com/udacity/Sunshine-Version-2/blob/sunshine_master/app/src/main/java/com/example/android/sunshine/app/data/WeatherProvider.java
         */
        if (selection == null) {
            selection = "1";
        }

        switch (match) {
            case MOVIE: {
                rowsDeleted = sqLiteDatabase.delete(MovieContract.Tables.MOVIE, selection, selectionArgs);
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (rowsDeleted > 0) {
            notifyChange(uri);
        }
        sqLiteDatabase.close();
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Match uri to retrieve all movies
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE, MOVIE);

        //Match uri with movie id
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    /**
     * Notify registered {@link android.database.ContentObserver}
     * that the database was modified either as:<br/>
     * 1. Insertion or deletion of a row<br/>
     * 2. Updating values in a row
     * @param uri Uri of changed content
     */
    private void notifyChange(Uri uri) {
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }
}
