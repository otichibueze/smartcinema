package com.chibusoft.smartcinema.Architecture;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import com.chibusoft.smartcinema.Models.Movies;

public class MovieViewModel extends ViewModel {

    public LiveData<PagedList<Movies.Results>> MoviePagedList;
    public LiveData<PageKeyedDataSource<Integer, Movies.Results>> liveDataSource;
    public MovieDataSourceFactory movieDataSourceFactory;

    public MovieViewModel() {

        MoviePagedList = CreateFilteredList("popular");

    }

    //Here we create a method that returns livedata.
    public LiveData<PagedList<Movies.Results>> CreateFilteredList(String sort)
    {
        movieDataSourceFactory = new MovieDataSourceFactory();
        movieDataSourceFactory.movieDataSource.sort = sort; //here we pass in sort type using movieDataSourceFactory
        liveDataSource = movieDataSourceFactory.getItemLiveDataSource();


        //Here we set our page list config
        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(MovieDataSource.PAGE_SIZE)
                        .build();

      return (new LivePagedListBuilder(movieDataSourceFactory, config)).build();
    }

    //Here is the method called when we want to sort data
    public void SortData(LifecycleOwner lifecycleOwner,String sort)
    {
        MoviePagedList.removeObservers(lifecycleOwner); //we remove old observer
        MoviePagedList = CreateFilteredList(sort); //we add new observer

    }
}
