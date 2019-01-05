package com.chibusoft.smartcinema.Utilities;

import com.chibusoft.smartcinema.Models.MovieReviews;
import com.chibusoft.smartcinema.Models.MovieVideos;
import com.chibusoft.smartcinema.Models.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface GetDataService {

    @GET("movie/{sort}")
    Call<Movies> getMovies(
            @Path("sort") String sort_by,
            @Query("api_key") String apiKey,
            @Query("page") int page_num
    );

    @GET("movie/{id}/videos?")
    Call<MovieVideos> getVideos(
            @Path("id") int movie_id,
            @Query("api_key") String apiKey
    );

    @GET("movie/{id}/reviews?")
    Call<MovieReviews> getReviews(
            @Path("id") int movie_id,
            @Query("api_key") String apiKey
    );


}
