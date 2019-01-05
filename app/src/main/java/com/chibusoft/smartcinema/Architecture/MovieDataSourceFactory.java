package com.chibusoft.smartcinema.Architecture;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import com.chibusoft.smartcinema.Models.Movies;

public class MovieDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Movies.Results>> movieLiveDataSource = new MutableLiveData<>();
    public MovieDataSource movieDataSource;


    @Override
    public DataSource create() {
        movieDataSource = new MovieDataSource();
        movieLiveDataSource.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Movies.Results>> getItemLiveDataSource() {
        return movieLiveDataSource;
    }


}

