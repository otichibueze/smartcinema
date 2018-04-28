package com.chibusoft.smartcinema.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.chibusoft.smartcinema.Data.MovieContract.MovieEntry;
import com.chibusoft.smartcinema.Data.MovieContract.MovieReviewsEntry;
import com.chibusoft.smartcinema.Data.MovieContract.MovieVideosEntry;

/**
 * Created by EBELE PC on 4/19/2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoriteMovies.db";

    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String SQL_CREATE_MOVIE_ENTRY_TABLE = "CREATE TABLE "
                + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"  +
                MovieEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieEntry.COLUMN_FAVORITE + " BOOLEAN NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_ENTRY_TABLE);

        final String SQL_CREATE_MOVIE_VIDEO_ENTRY_TABLE = "CREATE TABLE " +
                MovieVideosEntry.TABLE_NAME + " (" +
                MovieVideosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"  +
                MovieVideosEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                MovieVideosEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                MovieVideosEntry.COLUMN_TYPE + " TEXT NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_VIDEO_ENTRY_TABLE);

        final String SQL_CREATE_MOVIE_REVIEW_ENTRY_TABLE = "CREATE TABLE " +
                MovieReviewsEntry.TABLE_NAME + " (" +
                MovieReviewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"  +
                MovieReviewsEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                MovieReviewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                MovieReviewsEntry.COLUMN_CONTENT + " TEXT NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_REVIEW_ENTRY_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieVideosEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieReviewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
