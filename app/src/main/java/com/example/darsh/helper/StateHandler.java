package com.example.darsh.helper;

import android.content.Context;

import com.example.darsh.model.Movie;
import com.example.darsh.model.MovieReview;
import com.example.darsh.model.MovieVideo;
import com.example.darsh.popularmovies.R;

/**
 * Created by darshan on 10/6/16.
 */

/**
 * Helper class to deal with different error states
 * of the values for certain views to display. Returns
 * the required value for such variables depending on
 * the state. For instance, if there is a network error
 * intimate user to check if device is connected to the
 * Internet by setting the tagline text to the same.
 */
public class StateHandler {
    public static String handleMovieDetailState(Context context, int flag) {
        String tagLine;
        switch (flag) {
            case Constants.NETWORK_ERROR: {
                tagLine = context.getString(R.string.network_error);
                break;
            }

            case Constants.SERVER_ERROR: {
                tagLine = context.getString(R.string.server_error);
                break;
            }

            default: {
                tagLine = "";
            }
        }
        return tagLine;
    }

    public static MovieVideo handleMovieVideoState(Context context, int flag) {
        MovieVideo movieVideo = new MovieVideo();
        movieVideo.setKey("");
        switch (flag) {
            case Constants.NONE: {
                movieVideo.setName(context.getString(R.string.no_video));
                break;
            }

            case Constants.NETWORK_ERROR: {
                movieVideo.setName(context.getString(R.string.network_error_short));
                break;
            }

            case Constants.SERVER_ERROR: {
                movieVideo.setName(context.getString(R.string.server_error_short));
                break;
            }
        }
        return movieVideo;
    }

    public static Movie handleSimilarMovieState(Context context, int flag) {
        Movie movie = new Movie();
        movie.setPosterPath("");
        switch (flag) {
            case Constants.NONE: {
                movie.setTitle(context.getString(R.string.none_similar));
                break;
            }

            case Constants.NETWORK_ERROR: {
                movie.setTitle(context.getString(R.string.network_error_short));
                break;
            }

            case Constants.SERVER_ERROR: {
                movie.setTitle(context.getString(R.string.server_error_short));
                break;
            }
        }
        return movie;
    }

    public static MovieReview handleMovieReviewState(Context context, int flag) {
        MovieReview review = new MovieReview();
        review.setAuthor(context.getString(R.string.no_review));
        switch (flag) {
            case Constants.NONE: {
                review.setContent(context.getString(R.string.no_reviews));
                break;
            }

            case Constants.NETWORK_ERROR: {
                review.setContent(context.getString(R.string.network_error));
                break;
            }

            case Constants.SERVER_ERROR: {
                review.setContent(context.getString(R.string.server_error));
                break;
            }
        }
        return review;
    }
}
