package com.chibusoft.smartcinema;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by EBELE PC on 4/17/2018.
 */

public class MovieVideos implements Parcelable {

    public String key;


    public String type;

    MovieVideos(){}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private MovieVideos(Parcel in){
        key  = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(type);
    }

    public final Parcelable.Creator<MovieVideos> CREATOR = new Parcelable.Creator<MovieVideos>() {
        @Override
        public MovieVideos createFromParcel(Parcel parcel) {
            return new MovieVideos(parcel);
        }

        @Override
        public MovieVideos[] newArray(int i) {
            return new MovieVideos[i];
        }

    };

}
