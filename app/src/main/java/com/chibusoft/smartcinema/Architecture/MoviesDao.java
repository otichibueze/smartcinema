package com.chibusoft.smartcinema.Architecture;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;
import android.arch.paging.DataSource;


@Dao
public interface MoviesDao {

    @Query("SELECT * FROM movies ORDER BY id")
    LiveData<List<MoviesRoom>> loadAllMovies();

    @Query("SELECT * FROM movies ORDER BY id")
    DataSource.Factory<Integer,MoviesRoom> loadAllMoviesFactory();

    @Insert
    void insertMovies(MoviesRoom moviesRoom);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovies(MoviesRoom moviesRoom);


    @Delete
    void deleteMovies(MoviesRoom moviesRoom);

    @Query("Delete FROM movies WHERE `key` = :id")
    void DeleteMovieById(int id);

    @Query("SELECT COUNT(`key`) FROM movies")
    int GetCount();

    @Query("SELECT * FROM movies WHERE `key` = :id")
    LiveData<MoviesRoom> loadMovieById(int id);
    //The :id inside @Query takes the id from passed in the method loadTaskById
    //So essentially its an easy way to pass parameter from method to Query using :nameOfParameter
}

