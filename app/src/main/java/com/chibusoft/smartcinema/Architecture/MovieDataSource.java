package com.chibusoft.smartcinema.Architecture;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.chibusoft.smartcinema.MainActivity;
import com.chibusoft.smartcinema.Models.Movies;
import com.chibusoft.smartcinema.Utilities.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movies.Results> {

    public static final int PAGE_SIZE = 20;
    public static String sort; //this is used to load json either popular or top_rated
    private static final int FIRST_PAGE = 1;



    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movies.Results> callback) {


        RetrofitClientInstance.getInstance()
                .getApi()
                .getMovies(sort,MainActivity.KEY,FIRST_PAGE) //here we applied sort type
                .enqueue(new Callback<Movies>() {
                    @Override
                    public void onResponse(Call<Movies> call, Response<Movies> response) {

                        if(response.body() != null){
                            Movies movies = response.body();

                            //call back must be returned
                            callback.onResult(movies.results,null, FIRST_PAGE + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<Movies> call, Throwable t) {
                        Toast.makeText(MainActivity.getInstance(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movies.Results> callback) {


        RetrofitClientInstance.getInstance()
                .getApi()
                .getMovies(sort,MainActivity.KEY,params.key)
                .enqueue(new Callback<Movies>() {
                    @Override
                    public void onResponse(Call<Movies> call, Response<Movies> response) {

                        if(response.body() != null){

                            //here we minus page key if greater than 1 or null
                            Integer key = (params.key > 1) ? params.key - 1 : null;

                            callback.onResult(response.body().results,  key);
                        }
                    }

                    @Override
                    public void onFailure(Call<Movies> call, Throwable t) {
                        Toast.makeText(MainActivity.getInstance(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movies.Results> callback) {

       // final Integer key = params.key + 1;

        RetrofitClientInstance.getInstance()
                .getApi()
                .getMovies(sort,MainActivity.KEY,params.key)
                .enqueue(new Callback<Movies>() {
                    @Override
                    public void onResponse(Call<Movies> call, Response<Movies> response) {

                        if(response.body() != null){

                            Integer key = params.key < response.body().total_pages ? params.key + 1 : null;
                            callback.onResult(response.body().results,  key);
                        }
                    }

                    @Override
                    public void onFailure(Call<Movies> call, Throwable t) {
                        Toast.makeText(MainActivity.getInstance(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
