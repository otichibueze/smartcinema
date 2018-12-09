package com.chibusoft.smartcinema.Utilities;


import android.net.Uri;


import com.chibusoft.smartcinema.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by EBELE PC on 4/2/2018.
 */

public class NetworkUtils {

    private static final String BASE_URL ="https://api.themoviedb.org/3/movie";

    public static String SORT_BY = "";

    private final static String API_KEY = BuildConfig.MY_MOVIE_DB_API_KEY;

    private final static String PARAM_API = "api_key";

    private final static String VIDEO = "videos";

    private final static String REVIEWS = "reviews";

    private final static String PARAM_PAGE = "page";


    public static URL buildUrl(String sortby) {
        SORT_BY = sortby;

        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(SORT_BY)
                .appendQueryParameter(PARAM_API,API_KEY)
                .build();

        URL url = null;
        try
        {
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e)
        {
            e.printStackTrace();
        }


        return url;
    }

    public static URL buildUrlPage(String page) {

        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(SORT_BY)
                .appendQueryParameter(PARAM_API,API_KEY).appendQueryParameter(PARAM_PAGE,page)
                .build();

        URL url = null;
        try
        {
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e)
        {
            e.printStackTrace();
        }


        return url;
    }

    public static URL buildUrlVideo(String id) {

        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(VIDEO)
                .appendQueryParameter(PARAM_API,API_KEY)
                .build();

        URL url = null;
        try
        {
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlReviews(String id) {

        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(REVIEWS)
                .appendQueryParameter(PARAM_API,API_KEY)
                .build();

        URL url = null;
        try
        {
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return url;
    }



    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
