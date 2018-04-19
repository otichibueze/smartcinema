package com.chibusoft.smartcinema.Utilities;

/**
 * Created by EBELE PC on 4/18/2018.
 */



import com.chibusoft.smartcinema.MovieReviews;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BoxOfficeReviews {

    @SerializedName("results")
    public List<MovieReviews> movieReviewsList;


    public BoxOfficeReviews()
    {
        movieReviewsList = new ArrayList<>();
    }

    public static BoxOfficeReviews parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        BoxOfficeReviews boxOfficeReviews = gson.fromJson(response, BoxOfficeReviews.class);

        return boxOfficeReviews;
    }


}
