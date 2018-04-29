package com.chibusoft.smartcinema.Utilities;

/**
 * Created by EBELE PC on 4/18/2018.
 */

import com.chibusoft.smartcinema.MovieVideos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class BoxOfficeVideos {

    @SerializedName("results")
    public List<MovieVideos> movieVideosList;



    public BoxOfficeVideos()
    {
        movieVideosList = new ArrayList<>();
    }

    public static BoxOfficeVideos parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        BoxOfficeVideos boxOfficeVideos = gson.fromJson(response, BoxOfficeVideos.class);

        return boxOfficeVideos;
    }
}
