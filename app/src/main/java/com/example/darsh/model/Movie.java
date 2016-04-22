package com.example.darsh.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by darshan on 4/4/16.
 */
public class Movie implements Parcelable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("genre_ids")
    private ArrayList<Integer> genreIds = new ArrayList<>();

    @Expose
    @SerializedName("adult")
    private boolean adult;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("original_title")
    private String originalTitle;

    @Expose
    @SerializedName("original_language")
    private String originalLanguage;

    @Expose
    @SerializedName("release_date")
    private String releaseDate;

    @Expose
    @SerializedName("poster_path")
    private String posterPath;

    @Expose
    @SerializedName("backdrop_path")
    private String backdropPath;

    @Expose
    @SerializedName("overview")
    private String overview;

    @Expose
    @SerializedName("video")
    private boolean video;

    @Expose
    @SerializedName("popularity")
    private double popularity;

    @Expose
    @SerializedName("vote_count")
    private long voteCount;

    @Expose
    @SerializedName("vote_average")
    private double voteAverage;


    public Movie() {
    }

    private Movie(Parcel source) {
        this.id = source.readInt();
        this.genreIds = source.readArrayList(Integer.class.getClassLoader());
        if (genreIds == null) {
            genreIds = new ArrayList<>();
        }
        this.adult = source.readByte() == 1;
        this.title = source.readString();
        this.originalTitle = source.readString();
        this.originalLanguage = source.readString();
        this.releaseDate = source.readString();
        this.posterPath = source.readString();
        this.backdropPath = source.readString();
        this.overview = source.readString();
        this.video = source.readByte() == 1;
        this.popularity = source.readDouble();
        this.voteCount = source.readLong();
        this.voteAverage = source.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeList(genreIds);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeDouble(popularity);
        dest.writeLong(voteCount);
        dest.writeDouble(voteAverage);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(ArrayList<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
}
