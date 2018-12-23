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
public interface MoviesReviewsDao {
    @Query("SELECT * FROM movies_reviews ORDER BY id")
    LiveData<List<MoviesVideoRoom>> loadAllReviews();

    @Insert
    void insertReview(MovieReviewsRoom movieReviewsRoom);

    @Insert
    void insertReview(List<MovieReviewsRoom> movieReviewsRoom);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateReview(MovieReviewsRoom movieReviewsRoom);

    @Delete
    void deleteReview(MovieReviewsRoom movieReviewsRoom);

    @Query("DELETE FROM movies_reviews WHERE mkey = :id")
    void deleteReviewById(int id);

    //@Query("SELECT * FROM movies_reviews WHERE mkey = :id")
    //MovieReviewsRoom loadReviewById(int id);

    @Query("SELECT * FROM movies_reviews WHERE mkey = :id")
    LiveData<List<MovieReviewsRoom>> loadReviewById(int id);

}


