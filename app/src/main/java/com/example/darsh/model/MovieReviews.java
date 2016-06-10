package com.example.darsh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by darshan on 9/6/16.
 */
public class MovieReviews {
    @Expose
    @SerializedName("page")
    private int page;

    @Expose
    @SerializedName("results")
    private ArrayList<MovieReview> movieReviews = new ArrayList<>();

    @Expose
    @SerializedName("total_pages")
    private int totalPages;

    @Expose
    @SerializedName("total_results")
    private int totalResults;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<MovieReview> getMovieReviews() {
        return movieReviews;
    }

    public void setMovieReviews(ArrayList<MovieReview> movieReviews) {
        this.movieReviews = movieReviews;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
