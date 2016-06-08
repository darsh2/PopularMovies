package com.example.darsh.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 8/6/16.
 */
public class MovieVideo implements Parcelable {
    @Expose
    @SerializedName("key")
    private String key;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("site")
    private String site;

    public MovieVideo() {}

    private MovieVideo(Parcel source) {
        this.key = source.readString();
        this.name = source.readString();
        this.site = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
    }

    public static final Parcelable.Creator<MovieVideo> CREATOR = new Creator<MovieVideo>() {
        @Override
        public MovieVideo createFromParcel(Parcel source) {
            return new MovieVideo(source);
        }

        @Override
        public MovieVideo[] newArray(int size) {
            return new MovieVideo[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
