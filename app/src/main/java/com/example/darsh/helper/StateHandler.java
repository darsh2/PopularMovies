package com.example.darsh.helper;

import android.content.Context;

import com.example.darsh.model.MovieReview;
import com.example.darsh.popularmovies.R;

/**
 * Created by darshan on 10/6/16.
 */
public class StateHandler {
    public static MovieReview handleMovieReviewState(Context context, int flag) {
        MovieReview review = new MovieReview();
        review.setAuthor(context.getString(R.string.no_review));
        switch (flag) {
            case Constants.NONE: {
                review.setContent(context.getString(R.string.no_reviews));
                break;
            }

            case Constants.NETWORK_ERROR: {
                review.setContent(context.getString(R.string.network_error_movie_detail));
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
