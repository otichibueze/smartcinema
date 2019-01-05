package com.chibusoft.smartcinema.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Movies {

    public int page;

    public int total_results;

    public int total_pages;

    @SerializedName("results")
    public List<Results> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public class Results {

        @SerializedName("id")
        private String mId;

        @SerializedName("vote_average")
        private double mVote_average;

        @SerializedName("title")
        private String mTitle;

        @SerializedName("poster_path")
        private String mPoster_path;

        @SerializedName("overview")
        private String mOverview;

        @SerializedName("release_date")
        private String mRelease_date;


        private final String BASE_PATH = "https://image.tmdb.org/t/p/w185";


        public String getmId() {
            return mId;
        }

        public void setmId(String mId) {
            this.mId = mId;
        }

        public double getmVote_average() {
            return mVote_average;
        }

        public String getmTitle() {
            return mTitle;
        }

        public String getmPoster_path() {
            return BASE_PATH + mPoster_path;
        }

        public String getmPoster() {
            return mPoster_path;
        }


        public String getmOverview() {
            return mOverview;
        }

        public String getmRelease_date() {
            return mRelease_date;
        }

        public void setmVote_average(double mVote_average) {
            this.mVote_average = mVote_average;
        }

        public void setmTitle(String mTitle) {
            this.mTitle = mTitle;
        }

        public void setmPoster_path(String mPoster_path) {
            this.mPoster_path = BASE_PATH + mPoster_path;
        }

        public void setmOverview(String mOverview) {
            this.mOverview = mOverview;
        }

        public void setmRelease_date(String mRelease_date) {
            this.mRelease_date = mRelease_date;
        }

    }

}
