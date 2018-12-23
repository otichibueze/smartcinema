package com.chibusoft.smartcinema.Architecture;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieVideoDao {

    @Query("SELECT * FROM movies_videos ORDER BY id")
    LiveData<List<MoviesVideoRoom>> loadAllVideos();

    @Insert
    void insertVideo(MoviesVideoRoom moviesVideoRoom);

    @Insert
    void insertVideo(List<MoviesVideoRoom> moviesVideoRoom);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateVideo(MoviesVideoRoom moviesVideoRoom);

    @Delete
    void deleteVideo(MoviesVideoRoom moviesVideoRoom);

    @Query("Delete FROM movies_videos WHERE mkey = :id")
    void DeleteVideoById(int id);

    @Query("SELECT * FROM movies_videos WHERE mkey = :id")
    LiveData<List<MoviesVideoRoom>> loadVideoById(int id);

}


