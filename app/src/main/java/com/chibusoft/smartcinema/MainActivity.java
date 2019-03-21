package com.chibusoft.smartcinema;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.chibusoft.smartcinema.Adapters.MoviesAdapter;
import com.chibusoft.smartcinema.Adapters.RoomMovieAdapter;
import com.chibusoft.smartcinema.Architecture.AppDatabase;
import com.chibusoft.smartcinema.Architecture.MovieViewModel;
import com.chibusoft.smartcinema.Architecture.MoviesRoom;
import com.chibusoft.smartcinema.Architecture.RoomViewModel;
import com.chibusoft.smartcinema.Models.Movies;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends  AppCompatActivity implements OnSharedPreferenceChangeListener, MoviesAdapter.ItemClickListener
, RoomMovieAdapter.ItemClickListener{



    private static final int MOVIE_LOADER = 1;

    private static final int MOVIE_LOADER_PAGE = 2;

    private static final String SEARCH_QUERY_URL = "query";
    private static final int FAVORITE_LOADER = 4;
    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressBar mLoadingIndicator;

    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String POSTER = "POSTER";
    public static final String OVERVIEW = "OVERVIEW";
    public static final String RELEASE = "RELEASE";
    public static final String AVERAGE = "AVERAGE";
    public static final String LOAD_TYPE = "LOAD";

    //Database Instance
    private AppDatabase mDb;

    //MainActivity
    private static MainActivity instance;

    private MoviesAdapter movieAdapter;

    private Movies movieList;

    //private Movies[] movies;

    public static int load_Type = 1;

    public static String sort_movies_by;

    public final static String KEY = BuildConfig.MY_MOVIE_DB_API_KEY;

    @Nullable
    private CountingIdlingResource mIdlingResource= new CountingIdlingResource("Loading_Data");

    public int page = 1;

    public SharedPreferences sharedPreference;

    public MovieViewModel movieViewModel;

    public  RecyclerView mMovie_RV;

    public final MoviesAdapter adapter = new MoviesAdapter(this,this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        getIdlingResource();

        //Initialize member variable for the data base
        mDb = AppDatabase.getInstance(getApplicationContext());
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mMovie_RV = findViewById(R.id.rv_movies);//rv_movies from layout

       //spancount this tell the grid how many columns you want
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mMovie_RV.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else{
            mMovie_RV.setLayoutManager(new GridLayoutManager(this, 4));
        }


        //We set this to true to allows recyclerView to do optimization on our UI
        mMovie_RV.setHasFixedSize(true);

//        //Create shared preference
        setupSharedPreferences();


    }

    private void setupSharedPreferences() {
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreference.registerOnSharedPreferenceChangeListener(this);
        loadFromPreferences(sharedPreference);
    }

    ///Get insstance
    public static MainActivity getInstance() {
        return instance;
    }

    /**
     * This will load movies sort method from shared preference
     * @param sharedPreferences
     */
    public void loadFromPreferences(SharedPreferences sharedPreferences) {
        sort_movies_by = sharedPreferences.getString(getString(R.string.pref_sort_key), getResources().getString(R.string.pref_load_popular_value));

        //TODO i removed load code
        makeMovieSearchQuery(sort_movies_by);
    }

    private void makeMovieSearchQuery(String value) {

        if(value.equals("favorite"))
        {
            load_Type = 2;

            //This load data from viewModel using room as dataSource
            //Then paging library to load recyclerview adapter
            startFavoriteLoader();
        }
        else {
            load_Type = 1;
            page = 1;

            //This loads data from network using viewModel and dataSource
            //Inside dataSource we have retrofit that load info from network
            loadFromNetwork();

        }
    }

    public void loadFromNetwork()
    {
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        if(movieViewModel != null) {
            movieViewModel.SortData(this,sort_movies_by);

            movieViewModel.MoviePagedList.observe(this, new Observer<PagedList<Movies.Results>>() {
                @Override
                public void onChanged(@Nullable PagedList<Movies.Results> moviesList) {
                    adapter.submitList(moviesList);
                }
            });
        }

        mMovie_RV.setAdapter(adapter);
    }



    private  void startFavoriteLoader()
    {

        RoomViewModel roomViewModel =  ViewModelProviders.of(this).get(RoomViewModel.class);

        final RoomMovieAdapter roomMovieAdapter = new RoomMovieAdapter(this,this);

        roomViewModel.MovieRoomPagedList.observe(this, new Observer<PagedList<MoviesRoom>>() {
            @Override
            public void onChanged(@Nullable PagedList<MoviesRoom> moviesRoom) {

                roomMovieAdapter.submitList(moviesRoom);
            }
        });

        mMovie_RV.setAdapter(roomMovieAdapter);
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


    private void showErrorMessage()
    {
        Toast.makeText(this, "Please check your network" + "\n" + "Could not load data", Toast.LENGTH_LONG).show();
    }

    /**
     * We made load details activity to accept null and either take
     * movies.results to populated details view or using moviesRoom to
     * populate details view
     * @param movie Movies.Result
     * @param moviesRoom MoviesRooom
     */
    private void launchDetailActivity(@Nullable Movies.Results movie,@Nullable MoviesRoom moviesRoom)
    {
        Intent intent = new Intent(this , DetailsActivity.class);

        if(movie != null) {
            intent.putExtra(ID, movie.getmId());
            intent.putExtra(TITLE, movie.getmTitle());
            //if (load_Type == 1) intent.putExtra(POSTER, movie.getmPoster_path());
            //else intent.putExtra(POSTER, movie.getmPoster());

            intent.putExtra(POSTER, movie.getmPoster_path());
            intent.putExtra(OVERVIEW, movie.getmOverview());
            intent.putExtra(RELEASE, movie.getmRelease_date());
            intent.putExtra(AVERAGE, movie.getmVote_average());
            intent.putExtra(LOAD_TYPE, load_Type);
            startActivity(intent);
           // return;
        }
        else if(moviesRoom != null)
        {
            intent.putExtra(ID, String.valueOf(moviesRoom.getKey()));
            intent.putExtra(TITLE, moviesRoom.getTitle());
            intent.putExtra(POSTER, moviesRoom.getPoster_path());
            intent.putExtra(OVERVIEW, moviesRoom.getOverview());
            intent.putExtra(RELEASE, moviesRoom.getRelease_date());
            intent.putExtra(AVERAGE, moviesRoom.getVote_average());
            intent.putExtra(LOAD_TYPE, load_Type);
            startActivity(intent);
          //  return;
        }

    }


    private void showEmptyMessage() {
        Toast.makeText(this, "Favorites is empty", Toast.LENGTH_LONG).show();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }



    @Override
    public void onListItemClick(MoviesRoom movie) {
        launchDetailActivity(null, movie);
    }

    @Override
    public void onListItemClick(Movies.Results result)
    {
        launchDetailActivity(result, null);
    }

    @VisibleForTesting
    @NonNull
    public CountingIdlingResource getIdlingResource() {
        return mIdlingResource;
    }
}
