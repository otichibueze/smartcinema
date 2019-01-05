package com.chibusoft.smartcinema.Architecture;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.lifecycle.AndroidViewModel;


public class RoomViewModel extends AndroidViewModel {

    //Database Instance
    private AppDatabase mDb;
    public LiveData<PagedList<MoviesRoom>> MovieRoomPagedList;

    public RoomViewModel(Application application)
    {
        super(application);
        //Initialize member variable for the data base
        mDb = AppDatabase.getInstance(this.getApplication());

        //Here we set our page list config
        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(10)
                        .build();

        MovieRoomPagedList = (new LivePagedListBuilder(mDb.moviesDao().loadAllMoviesFactory(), config)).build();

    }


}
