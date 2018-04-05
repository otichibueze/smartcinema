package com.chibusoft.smartcinema;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by EBELE PC on 4/1/2018.
 */

public class Movies implements Parcelable {
    //private int mVote_count;
    //private int mId;
    //private boolean mVideo;
    private double mVote_average;
    private String mTitle;
    //private double mPopularity;
    private String mPoster_path ;
    //private String mOriginal_language;
    //private String mOriginal_title;
    //private int[] mGenre_ids;
    //private String mBackdrop_path;
    //private boolean mAdult;
    private String mOverview;
    private String mRelease_date;


    private final String BASE_PATH = "http://image.tmdb.org/t/p/w185";

    private Movies(Parcel in)
    {
        mTitle = in.readString();
        mPoster_path = in.readString();
        mOverview = in.readString();
        mRelease_date = in.readString();
        mVote_average = in.readDouble();
    }

    Movies(){}

//    Movies(int vote_count, int id, boolean video, double mVote_average, String title, double popularity, String poster_path, String original_language,
//           String original_title, int[] genre_ids, String backdrop_path, boolean adult, String overview, String release_date)
//    {
//       this.mVote_count = vote_count;
//       this.mId = id;
//       this.mVideo = video;
//       this.mVote_average = mVote_average;
//       this.mTitle = title;
//       this.mPopularity = popularity;
//       this.mPoster_path = BASE_PATH + poster_path;
//       this.mOriginal_language =original_language;
//       this.mOriginal_title = original_title;
//       this.mGenre_ids = genre_ids;
//       this.mBackdrop_path = backdrop_path;
//       this.mAdult = adult;
//       this.mOverview = overview;
//       this.mRelease_date = release_date;
//    }


//    public int getmVote_count() {
//        return mVote_count;
//    }

//    public int getmId() {
//        return mId;
//    }

//    public boolean ismVideo() {
//        return mVideo;
//    }

    public double getmVote_average() {
        return mVote_average;
    }

    public String getmTitle() {
        return mTitle;
    }

//    public double getmPopularity() {
//        return mPopularity;
//    }

    public String getmPoster_path() {
        return mPoster_path;
    }

//    public String getmOriginal_language() {
//        return mOriginal_language;
//    }

//    public String getmOriginal_title() {
//        return mOriginal_title;
//    }

//    public int[] getmGenre_ids() {
//        return mGenre_ids;
//    }

//    public String getmBackdrop_path() {
//        return mBackdrop_path;
//    }

//    public boolean ismAdult() {
//        return mAdult;
//    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmRelease_date() {
        return mRelease_date;
    }



//    public void setmVote_count(int mVote_count) {
//        this.mVote_count = mVote_count;
//    }

//    public void setmId(int mId) {
//        this.mId = mId;
//    }

//    public void setmVideo(boolean mVideo) {
//        this.mVideo = mVideo;
//    }

    public void setmVote_average(double mVote_average) {
        this.mVote_average = mVote_average;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

//    public void setmPopularity(double mPopularity) {
//        this.mPopularity = mPopularity;
//    }

    public void setmPoster_path(String mPoster_path) {
        this.mPoster_path = BASE_PATH + mPoster_path;
    }

//    public void setmOriginal_language(String mOriginal_language) {
//        this.mOriginal_language = mOriginal_language;
//    }

//    public void setmOriginal_title(String mOriginal_title) {
//        this.mOriginal_title = mOriginal_title;
//    }

//    public void setmGenre_ids(int[] mGenre_ids) {
//        this.mGenre_ids = mGenre_ids;
//    }

//    public void setmBackdrop_path(String mBackdrop_path) {
//        this.mBackdrop_path = mBackdrop_path;
//    }

//    public void setmAdult(boolean mAdult) {
//        this.mAdult = mAdult;
//    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public void setmRelease_date(String mRelease_date) {
        this.mRelease_date = mRelease_date;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mPoster_path);
        parcel.writeString(mOverview);
        parcel.writeString(mRelease_date);
        parcel.writeDouble(mVote_average);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel parcel) {
            return new Movies(parcel);
        }

        @Override
        public Movies[] newArray(int i) {
            return new Movies[i];
        }

    };


}
