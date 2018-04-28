package com.chibusoft.smartcinema.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by EBELE PC on 4/19/2018.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.chibusoft.smartcinema";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static final String PATH_MOVIE_VIDEO = "movievideo";

    public static final String PATH_MOVIE_REVIEW = "moviereview";


    private MovieContract () {}

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();


        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_FAVORITE = "favorite";
    }

    public static final class MovieVideosEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_VIDEO).build();

        public static final String TABLE_NAME = "movievideo";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_TYPE = "type";
    }

    public static final class MovieReviewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_REVIEW).build();

        public static final String TABLE_NAME = "moviereview";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
    }

}
