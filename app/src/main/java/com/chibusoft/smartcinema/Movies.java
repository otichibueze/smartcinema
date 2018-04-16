package com.chibusoft.smartcinema;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


/**
 * Created by EBELE PC on 4/1/2018.
 */

public class Movies implements Parcelable {

    @SerializedName("id")
    private int mId;

    @SerializedName("vote_average")
    private double mVote_average;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("poster_path")
    private String mPoster_path ;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("release_date")
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

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
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



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
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
