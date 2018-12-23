package com.chibusoft.smartcinema.Architecture;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movies")
public class MoviesRoom {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private int key;
    private double vote_average;
    private String title;
    private String poster_path ;
    private String overview;
    private String release_date;
    private Boolean favorite;

    @Ignore
    public MoviesRoom(int key, double vote_average,String title,String poster_path,String overview,String release_date,
                      boolean favorite) {
        this.key = key;
        this.vote_average = vote_average;
        this.title = title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.favorite = favorite;
    }

    public MoviesRoom(int id,int key, double vote_average,String title,String poster_path,String overview,String release_date,
                      boolean favorite) {
        this.id = id;
        this.key = key;
        this.vote_average = vote_average;
        this.title = title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
