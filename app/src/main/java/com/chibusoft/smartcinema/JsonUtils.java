package com.chibusoft.smartcinema;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by EBELE PC on 4/2/2018.
 */

public class JsonUtils {

    private static JSONObject MovieJson;

    private static Movies[] movies;

    private final static String RESULT_KEY = "results";

    // private final static String VOTE_COUNT_KEY = "vote_count";

    //private final static String ID_KEY = "id";

    //private final static String VIDEO_KEY = "video";

    private final static String VOTE_AVERAGE_KEY = "vote_average";

    private final static String TITLE_KEY = "title";

    //private final static String POPULARITY_KEY = "popularity";

    private final static String POSTER_PATH_KEY = "poster_path";

    //private final static String ORIGINAL_LANUAGE = "original_language";

    //private final static String ORIGINAL_TITLE_KEY = "original_title";

    //private final static String GENRE_IDS_KEY = "genre_ids";

    //private final static String BACKDROP_PATH_KEY = "backdrop_path";

    //private final static String ADULT_KEY = "adult";

    private final static String OVERVIEW_KEY = "overview";

    private final static String RELEASE_DATE_KEY = "release_date";

    public static Movies[] parseMovieJson(String json) {

        try {
            MovieJson = new JSONObject(json);

            //get result[] count
            JSONArray result = MovieJson.getJSONArray(RESULT_KEY);

            //create new array based on result count
            movies = new Movies[result.length()];

            for(int i = 0; i <result.length(); i++)
            {
                movies[i] = new Movies();
                movies[i].setmTitle(result.getJSONObject(i).optString(TITLE_KEY));
                movies[i].setmPoster_path(result.getJSONObject(i).optString(POSTER_PATH_KEY));
                movies[i].setmOverview(result.getJSONObject(i).optString(OVERVIEW_KEY));
                movies[i].setmVote_average(result.getJSONObject(i).optDouble(VOTE_AVERAGE_KEY));
                movies[i].setmRelease_date(result.getJSONObject(i).optString(RELEASE_DATE_KEY));

            }

            return movies;
        }
        catch (JSONException e)
        {
            e.getStackTrace();
            return  null;
        }

    }

}
