package com.chibusoft.smartcinema.Utilities;

import android.net.Uri;

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

    private static final String BASE_URL ="http://api.themoviedb.org/3/movie";

    //final static String POPULAR = "popular";

    //final static String TOP_RATED = "top_rated";

    public static String SORT_BY = "";

    private final static String API_KEY = "your_key";

    private final static String PARAM_API = "api_key";



    //need to set by setting
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
