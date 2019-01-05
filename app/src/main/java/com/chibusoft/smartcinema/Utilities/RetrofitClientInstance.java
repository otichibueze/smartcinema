package com.chibusoft.smartcinema.Utilities;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static RetrofitClientInstance mInstance;


    private RetrofitClientInstance() {

      //  boxOfficeMovies = new BoxOfficeMovies();
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClientInstance getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClientInstance();
        }
        return mInstance;
    }

    public GetDataService getApi(){
        return retrofit.create(GetDataService.class);
    }


}
