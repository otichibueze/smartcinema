package com.chibusoft.smartcinema.Utilities;

/**
 * Created by EBELE PC on 4/16/2018.
 */

import com.chibusoft.smartcinema.Movies;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BoxOfficeMovies
{

    @SerializedName("results")
   public List<Movies> movieList;



     public BoxOfficeMovies()
    {
        movieList = new ArrayList<>();
    }

    public static BoxOfficeMovies parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        BoxOfficeMovies boxOfficeMovies = gson.fromJson(response, BoxOfficeMovies.class);

        return boxOfficeMovies;
    }


}
