package com.example.darsh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 23/6/16.
 */
public class Crew {
    @Expose
    @SerializedName("department")
    private String department;

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("job")
    private String job;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("profile_path")
    private String profilePath;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
