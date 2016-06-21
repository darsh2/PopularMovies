package com.example.darsh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by darshan on 4/4/16.
 */
public class MoviesList {
    @Expose
    @SerializedName("page")
    private int page;

    @Expose
    @SerializedName("results")
    private ArrayList<Movie> movies;

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

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
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
