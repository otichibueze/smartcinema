package com.chibusoft.smartcinema;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by EBELE PC on 4/17/2018.
 */

public class MovieReviews implements Parcelable {

    public String author;

    public String content;

    MovieReviews(){}

    MovieReviews(String Author,String Content){
        author = Author;
        content = Content;
    }


    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }



    private MovieReviews(Parcel in){
        author  = in.readString();
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
    }


    @Override
    public int describeContents() {
        return 0;
    }


    public final Parcelable.Creator<MovieReviews> CREATOR = new Parcelable.Creator<MovieReviews>() {
        @Override
        public MovieReviews createFromParcel(Parcel parcel) {
            return new MovieReviews(parcel);
        }

        @Override
        public MovieReviews[] newArray(int i) {
            return new MovieReviews[i];
        }

    };

}
