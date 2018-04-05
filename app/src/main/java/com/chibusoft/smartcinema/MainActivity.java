package com.chibusoft.smartcinema;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends  AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String>, OnSharedPreferenceChangeListener {



    private static final int MOVIE_LOADER = 1;

    private static final String SEARCH_QUERY_URL = "query";

    private ProgressBar mLoadingIndicator;


    public static final String TITLE = "TITLE";
    public static final String POSTER = "POSTER";
    public static final String OVERVIEW = "OVERVIEW";
    public static final String RELEASE = "RELEASE";
    public static final String AVERAGE = "AVERAGE";


    private MoviesAdapter movieAdapter;

    private GridView gridView;

    private ArrayList<Movies> movieList;

    private Movies[] movies;

    private String sort_movies_by;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSharedPreferences();
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        // Get a reference to the ListView, and attach this adapter to it.
         gridView =  findViewById(R.id.movies_grid);

        //if (savedInstanceState != null) {
         //   String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL);
        //}



        /*
         * Initialize the loader
         */
        getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this);

       // makeMovieSearchQuery();
    }

    private void setupSharedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadFromPreferences(sharedPreferences);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadFromPreferences(SharedPreferences sharedPreferences) {
        sort_movies_by = sharedPreferences.getString(getString(R.string.pref_sort_key),
                getResources().getString(R.string.pref_load_popular_value));

        makeMovieSearchQuery();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(getString(R.string.pref_sort_key)))
        {
            loadFromPreferences(sharedPreferences);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemselected = item.getItemId();

        //item id created on menu/main
        if(itemselected == R.id.action_settings)
        {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    private void makeMovieSearchQuery() {
       // String sortby = "popular"; // set sort by

        URL movieSearchUrl = NetworkUtils.buildUrl(sort_movies_by);

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL,movieSearchUrl.toString());


        LoaderManager loaderManager = getSupportLoaderManager();

        Loader<String> movieSearchLoader = loaderManager.getLoader(MOVIE_LOADER);
        if(movieSearchLoader == null)
        {
            loaderManager.initLoader(MOVIE_LOADER,queryBundle,this);
        }
        else
        {
            loaderManager.restartLoader(MOVIE_LOADER,queryBundle,this);
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("savedMovies", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState == null || !savedInstanceState.containsKey("savedMovies")) {
            movieList = new ArrayList<>(Arrays.asList(movies));
        }
        else {
            movieList = savedInstanceState.getParcelableArrayList("savedMovies");
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id,final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String mMovieJson ;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args == null)
                {
                    return;
                }


                mLoadingIndicator.setVisibility(View.VISIBLE);


                if(mMovieJson != null )
                {
                    deliverResult(mMovieJson);
                }
                else
                {
                    forceLoad(); //force load in background
                    //put code on pre-execute here
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public String loadInBackground() {

                String searchQueryUrlString = args.getString(SEARCH_QUERY_URL);
                if(searchQueryUrlString == null || TextUtils.isEmpty(searchQueryUrlString)) {
                    return null;
                }
                try {
                    URL result = new URL(searchQueryUrlString);
                    return NetworkUtils.getResponseFromHttpUrl(result);
                } catch (IOException e) {
                    //showErrorMessage();
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(String movieJson) {
                mMovieJson = movieJson;
                super.deliverResult(movieJson);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null && !data.equals("")) {
           parseJsonDataView( data.toString());
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private void showErrorMessage()
    {
        Toast.makeText(this, "Please check your network" + "\n" + "Could not load data", Toast.LENGTH_LONG).show();
    }

    private void parseJsonDataView(String data)
    {
        movies = JsonUtils.parseMovieJson(data);
        movieList = new ArrayList<>(Arrays.asList(movies));

        movieAdapter = new MoviesAdapter(this, movieList);

        gridView.setAdapter(movieAdapter);

        //onclick
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                launchDetailActivity(position);

            }
        });
    }

    private void launchDetailActivity(int i)
    {
        Intent intent = new Intent(this , DetailsActivity.class);
        intent.putExtra(TITLE, movieList.get(i).getmTitle());
        intent.putExtra(POSTER, movieList.get(i).getmPoster_path());
        intent.putExtra(OVERVIEW, movieList.get(i).getmOverview());
        intent.putExtra(RELEASE, movieList.get(i).getmRelease_date());
        intent.putExtra(AVERAGE, movieList.get(i).getmVote_average());
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
