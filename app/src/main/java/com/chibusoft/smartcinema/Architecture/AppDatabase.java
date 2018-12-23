package com.chibusoft.smartcinema.Architecture;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {MoviesRoom.class, MovieReviewsRoom.class, MoviesVideoRoom.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase  {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movies";  //database name
    private static AppDatabase sInstance; //used to create database only once

    public  static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }


    public abstract MoviesDao moviesDao();
    public abstract MovieVideoDao movieVideoDao();
    public abstract MoviesReviewsDao moviesReviewsDao();
}
