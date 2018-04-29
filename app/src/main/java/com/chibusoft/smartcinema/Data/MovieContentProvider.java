package com.chibusoft.smartcinema.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chibusoft.smartcinema.Data.MovieContract.MovieEntry;
import com.chibusoft.smartcinema.Data.MovieContract.MovieVideosEntry;
import com.chibusoft.smartcinema.Data.MovieContract.MovieReviewsEntry;


/**
 * Created by EBELE PC on 4/23/2018.
 */

public class MovieContentProvider extends ContentProvider {

    public MovieDbHelper dbHelper;

    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_ID = 101;

    public static final int MOVIE_VIDEO = 200;
    public static final int MOVIE_VIDEO_WITH_ID = 201;

    public static final int MOVIE_REVIEW  = 300;
    public static final int MOVIE_REVIEW_WITH_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(this.getContext());

        return  true;
    }


    public static UriMatcher buildUriMatcher()
    {
        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIE, MOVIE);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIE + "/#",MOVIE_WITH_ID);

        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIE_VIDEO, MOVIE_VIDEO);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIE_VIDEO + "/#",MOVIE_VIDEO_WITH_ID);

        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIE_REVIEW, MOVIE_REVIEW);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIE_REVIEW + "/#",MOVIE_REVIEW_WITH_ID);

        return  uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        //Cursor used to query
        Cursor retCursor;

        switch (match) {

            case MOVIE:

                retCursor =  db.query(MovieEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        sortOrder);
                break;

            //Query db with where clause
            case MOVIE_WITH_ID:

                //getPathSegments().get(1) this will get number after task "#"
                String id = uri.getPathSegments().get(1);

               String mselection = "id=?";
               String[] mSelectionArgs = new String[]{id};

                retCursor =  db.query(MovieEntry.TABLE_NAME,
                        projection,
                        mselection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            case MOVIE_REVIEW_WITH_ID:

                //getPathSegments().get(1) this will get number after task "#"
                String id_Review = uri.getPathSegments().get(1);

                String mselection_review = "id=?";
                String[] mSelectionArgs_review = new String[]{id_Review};

                retCursor =  db.query(MovieReviewsEntry.TABLE_NAME,
                        null,
                        mselection_review,
                        mSelectionArgs_review,
                        null,
                        null,
                        sortOrder);

                break;

            case MOVIE_VIDEO_WITH_ID:

                //getPathSegments().get(1) this will get number after task "#"
                String id_Video = uri.getPathSegments().get(1);

                String mselection_video = "id=?";
                String[] mSelectionArgs_video = new String[]{id_Video};

                retCursor =  db.query(MovieVideosEntry.TABLE_NAME,
                        null,
                        mselection_video,
                        mSelectionArgs_video,
                        null,
                        null,
                        sortOrder);

                break;

            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch(match){
            case MOVIE:
                //Inserting values into db movie table
                long id_movie = db.insert(MovieEntry.TABLE_NAME, null, contentValues);
                if(id_movie > 0) {
                    //success
                    returnUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id_movie);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into movie table " + uri);
                }

                break;

            case MOVIE_VIDEO:
                //Inserting values into db movie table
                long id_movie_video = db.insert(MovieVideosEntry.TABLE_NAME, null, contentValues);
                if(id_movie_video > 0) {
                    //success
                    returnUri = ContentUris.withAppendedId(MovieVideosEntry.CONTENT_URI, id_movie_video);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into movie video table " + uri);
                }

                break;

            case MOVIE_REVIEW:
                //Inserting values into db movie table
                long id_movie_review = db.insert(MovieReviewsEntry.TABLE_NAME, null, contentValues);
                if(id_movie_review > 0) {
                    //success
                    returnUri = ContentUris.withAppendedId(MovieReviewsEntry.CONTENT_URI, id_movie_review);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into movie review table " + uri);
                }

                break;

            //Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        //Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case MOVIE_REVIEW:
                db.beginTransaction();
                int rowsInserted_Review = 0;
                try {
                    for (ContentValues value : values) {
                        long _id_Review = db.insert(MovieReviewsEntry.TABLE_NAME, null, value);
                        if (_id_Review != -1) {
                            rowsInserted_Review++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted_Review > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                //Return the number of rows inserted from our implementation of bulkInsert
                return rowsInserted_Review;

            case MOVIE_VIDEO:
                db.beginTransaction();
                int rowsInserted_Video = 0;
                try {
                    for (ContentValues value : values) {
                        long _id_Video = db.insert(MovieVideosEntry.TABLE_NAME, null, value);
                        if (_id_Video != -1) {
                            rowsInserted_Video++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted_Video > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                // Return the number of rows inserted from our implementation of bulkInsert
                return rowsInserted_Video;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int EntryDeleted;

        // Get the task ID from the URI path
        String id = uri.getPathSegments().get(1);

        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case MOVIE_WITH_ID:

                EntryDeleted = db.delete(MovieEntry.TABLE_NAME, "id=?", new String[]{id});
                break;

            case MOVIE_REVIEW_WITH_ID:

                EntryDeleted = db.delete(MovieReviewsEntry.TABLE_NAME, "id=?", new String[]{id});
                break;

            case MOVIE_VIDEO_WITH_ID:

                EntryDeleted = db.delete(MovieVideosEntry.TABLE_NAME, "id=?", new String[]{id});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (EntryDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return EntryDeleted;
    }



    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
