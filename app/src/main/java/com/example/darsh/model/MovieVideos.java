package com.example.darsh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by darshan on 7/6/16.
 */
public class MovieVideos {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("results")
    private ArrayList<MovieVideo> videos = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<MovieVideo> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<MovieVideo> videos) {
        this.videos = videos;
    }
}