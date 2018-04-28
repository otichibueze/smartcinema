package com.chibusoft.smartcinema;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.support.v4.app.LoaderManager;

import android.support.v4.content.AsyncTaskLoader;

import android.support.v4.content.Loader;
import android.widget.Toast;

import com.chibusoft.smartcinema.Data.MovieContract;
import com.chibusoft.smartcinema.Data.MovieContract.MovieEntry;
import com.chibusoft.smartcinema.Data.MovieContract.MovieReviewsEntry;
import com.chibusoft.smartcinema.Data.MovieContract.MovieVideosEntry;
import com.chibusoft.smartcinema.Utilities.BoxOfficeReviews;
import com.chibusoft.smartcinema.Utilities.BoxOfficeVideos;
import com.chibusoft.smartcinema.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;

//implements LoaderManager.LoaderCallbacks<String>
public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    @BindView(R.id.title_details)
    TextView title;
    @BindView(R.id.release_details)
    TextView release;
    @BindView(R.id.vote_details)
    TextView vote;
    @BindView(R.id.synopsis_details)
    TextView synopsis;

    @BindView(R.id.movieimage_details)
    ImageView movieimage;

    private String image_link;

    @BindView(R.id.favorite_btn)
    ImageButton favorite_button;

    private boolean isFavorite;

    private static final int IS_FAVORITE_LOADER = 18;

    private static final String ID_SEARCH_QUERY = "id_query";

    private String mID;

    private static final int VIDEO_SEARCH_LOADER = 22;

    private static final String VIDEO_SEARCH_QUERY = "video_query";

    private ArrayList<MovieVideos> movieVideos;

    private BoxOfficeVideos boxOfficeVideos;

    private static final int REVIEWS_SEARCH_LOADER = 20;

    private static final String REVIEWS_SEARCH_QUERY = "reviews_query";

    private ArrayList<MovieReviews> movieReviews;

    private BoxOfficeReviews boxOfficeReviews;

    private MovieVideosAdapter movieVideosAdapter;

    private MovieReviewsAdapter movieReviewsAdapter;

    private ListView listViewVideo;

    private ListView listViewReview;

    public static final String VIDEO_LINK = "https://www.youtube.com/watch?v=";

    private static final String TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);


        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if (intent.hasExtra(MainActivity.ID)) {
            mID = intent.getStringExtra(MainActivity.ID);
            makeReviewSearchQuery(mID);
            makeVideoSearchQuery(mID);
           loadIsFavorite(mID);
        }

        if (intent.hasExtra(MainActivity.TITLE))
            title.setText(intent.getStringExtra(MainActivity.TITLE));

        if (intent.hasExtra(MainActivity.POSTER)) {
            image_link = intent.getStringExtra(MainActivity.POSTER);

            Picasso.with(this)
                    .load(image_link)
                    .placeholder(R.drawable.loadingmage)
                    .error(R.drawable.errorimage)
                    .into(movieimage);
        }

        if (intent.hasExtra(MainActivity.OVERVIEW))
            synopsis.setText(intent.getStringExtra(MainActivity.OVERVIEW));

        if (intent.hasExtra(MainActivity.RELEASE))
            release.setText(intent.getStringExtra(MainActivity.RELEASE));

        if (intent.hasExtra(MainActivity.AVERAGE))
            vote.setText(String.valueOf(intent.getDoubleExtra(MainActivity.AVERAGE, 0)));

        boxOfficeVideos = new BoxOfficeVideos();

        boxOfficeReviews = new BoxOfficeReviews();

        listViewVideo = (ListView) findViewById(R.id.listview_trailer);

        listViewReview = (ListView) findViewById(R.id.listview_reviews);

        //isFavorite = false;

    }

    public void showFavorite(boolean value)
    {
        if (value)
            favorite_button.setImageResource(R.drawable.ic_favorite_black_48dp);
        else
            favorite_button.setImageResource(R.drawable.ic_favorite_border_black_48dp);
    }

    public void addFavorite(View v) {
        String result = "Added to Favorites";
        isFavorite = isFavorite == true ? false : true;

        if (isFavorite) {
            ContentValues contentValues = new ContentValues();

            //This takes in a put string that identifies the kind of data being put and the data
            contentValues.put(MovieEntry.COLUMN_ID, mID);
            contentValues.put(MovieEntry.COLUMN_TITLE, title.getText().toString());
            contentValues.put(MovieEntry.COLUMN_POSTER_PATH, image_link);
            contentValues.put(MovieEntry.COLUMN_OVERVIEW, synopsis.getText().toString());
            contentValues.put(MovieEntry.COLUMN_DATE, release.getText().toString());
            contentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, vote.getText().toString());
            contentValues.put(MovieEntry.COLUMN_FAVORITE, isFavorite);

            Uri uri = getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);

            if (uri != null) {
                result = "Added " + title.getText() + " to favorites and %1s videos and %2s reviews";
                contentValues.clear();
            }

            int rowsVid = 0;
            if(movieVideos.size() > 0) {
                ContentValues[] contentValuesVideos = new ContentValues[movieVideos.size()];

                for (int i = 0; i < movieVideos.size(); i++) {
                    contentValuesVideos[i] = new ContentValues();
                    contentValuesVideos[i].put(MovieVideosEntry.COLUMN_ID, mID);
                    contentValuesVideos[i].put(MovieVideosEntry.COLUMN_KEY, movieVideos.get(i).getKey());
                    contentValuesVideos[i].put(MovieVideosEntry.COLUMN_TYPE, movieVideos.get(i).getType());
                }

                rowsVid = getContentResolver().bulkInsert(MovieVideosEntry.CONTENT_URI, contentValuesVideos);

            }

            int rowsReviews = 0;
            if(movieReviews!= null && movieReviews.size() > 0) {
                ContentValues[] contentValuesReviews = new ContentValues[movieReviews.size()];

                for (int i = 0; i < movieReviews.size(); i++) {
                    contentValuesReviews[i] = new ContentValues();
                    contentValuesReviews[i].put(MovieReviewsEntry.COLUMN_ID, mID);
                    contentValuesReviews[i].put(MovieReviewsEntry.COLUMN_AUTHOR, movieReviews.get(i).getAuthor());
                    contentValuesReviews[i].put(MovieReviewsEntry.COLUMN_CONTENT, movieReviews.get(i).getContent());
                }

                rowsReviews = getContentResolver().bulkInsert(MovieReviewsEntry.CONTENT_URI, contentValuesReviews);

            }

            Toast.makeText(getBaseContext(), String.format(result), Toast.LENGTH_LONG).show();

        }
        else
        {
            result = "Removed from Favorites";
            Uri uri = MovieEntry.CONTENT_URI.buildUpon().appendPath(mID).build();

            int delMovie = getContentResolver().delete(uri, null, null);

            Uri uriReview = MovieReviewsEntry.CONTENT_URI.buildUpon().appendPath(mID).build();

            int delReview =  getContentResolver().delete(uriReview, null, null);

            Uri uriVideo = MovieVideosEntry.CONTENT_URI.buildUpon().appendPath(mID).build();

            int delVideo = getContentResolver().delete(uriVideo, null, null);

            Toast.makeText(getBaseContext(), String.format(result), Toast.LENGTH_LONG).show();
        }

        showFavorite(isFavorite);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }





    private void parseJsonDataVideo(ArrayList<MovieVideos> data)
    {
        for(int i = data.size() - 1; i > -1; i--)
        {
            if(!data.get(i).type.equals("Trailer")) {
                data.remove(i);
            }
        }

        movieVideos = data;

        movieVideosAdapter = new MovieVideosAdapter(this, movieVideos);

        listViewVideo.setAdapter(movieVideosAdapter);

        int totalHeight = 0;
        for (int i = 0; i < movieVideosAdapter.getCount(); i++) {
            View listItem = movieVideosAdapter.getView(i, null, listViewVideo);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listViewVideo.getLayoutParams();
        params.height = totalHeight + (listViewVideo.getDividerHeight() * (listViewVideo.getCount() - 1));
        listViewVideo.setLayoutParams(params);
        listViewVideo.requestLayout();

        //On click of listView
        listViewVideo.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                      String key = movieVideos.get(position).getKey();
                      String weblink = VIDEO_LINK + key;
                      openWebPage(weblink);
            }
        });
    }

    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    private void parseJsonDataReview(ArrayList<MovieReviews> data)
    {
        movieReviewsAdapter = new MovieReviewsAdapter(this, data);

        listViewReview.setAdapter(movieReviewsAdapter);
        movieReviewsAdapter.notifyDataSetChanged();


        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listViewReview.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < movieReviewsAdapter.getCount(); i++) {
            View listItem = movieReviewsAdapter.getView(i, null, listViewReview);
            // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
            listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
                    listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listViewReview.getLayoutParams();
        params.height = totalHeight + (listViewReview.getDividerHeight() * (listViewReview.getCount() - 1 ));
        listViewReview.setLayoutParams(params);
       listViewReview.requestLayout();

    }


    private void showErrorMessage()
    {
        Toast.makeText(this, "Please check your network" + "\n" + "Could not load data", Toast.LENGTH_LONG).show();
    }


    private void makeVideoSearchQuery(String id) {

        URL videoSearchUrl = NetworkUtils.buildUrlVideo(id);

        Bundle queryBundle = new Bundle();
        queryBundle.putString(VIDEO_SEARCH_QUERY,videoSearchUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();

        Loader<String> videoSearchLoader = loaderManager.getLoader(VIDEO_SEARCH_LOADER);
        if(videoSearchLoader == null)
        {
            loaderManager.initLoader(VIDEO_SEARCH_LOADER,queryBundle,this);
        }
        else
        {
          loaderManager.restartLoader(VIDEO_SEARCH_LOADER,queryBundle,this);
        }
    }


    private void makeReviewSearchQuery(String id) {

        URL reviewSearchUrl = NetworkUtils.buildUrlReviews(id);

        Bundle queryBundle = new Bundle();
        queryBundle.putString(REVIEWS_SEARCH_QUERY,reviewSearchUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();

        Loader<String> reviewSearchLoader = loaderManager.getLoader(REVIEWS_SEARCH_LOADER);
        if(reviewSearchLoader == null)
        {
          loaderManager.initLoader(REVIEWS_SEARCH_LOADER,queryBundle,this);
        }
        else
        {
            loaderManager.restartLoader(REVIEWS_SEARCH_LOADER,queryBundle,this);
        }
    }


    @Override
    public Loader<String> onCreateLoader(int id,final Bundle arg) {
        return new AsyncTaskLoader<String>(this) {

            String mVideoJson, mReviewJson;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (arg == null) {
                    return;
                }


                //Cache data
                if (mVideoJson != null || mReviewJson != null) {
                    if (VIDEO_SEARCH_LOADER == getId())
                        deliverResult(mVideoJson);
                    if (REVIEWS_SEARCH_LOADER == getId())
                        deliverResult(mReviewJson);
                } else {
                    forceLoad(); //force load in background
                    //put code on pre-execute here
                }


            }


            @Override
            public String loadInBackground() {
				/* Here we load our search URL from saved bundle using it keys SEARCH_QUERY_URL_EXTRA*/

                int loaderID = getId();

                if (loaderID == VIDEO_SEARCH_LOADER) {
                    String searchVideoQueryUrl = arg.getString(VIDEO_SEARCH_QUERY);
                    if (searchVideoQueryUrl == null || TextUtils.isEmpty(searchVideoQueryUrl)) {
                        return null;
                    }
                    try {
                        URL videoUrl = new URL(searchVideoQueryUrl);
                        return  NetworkUtils.getResponseFromHttpUrl(videoUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                if (loaderID == REVIEWS_SEARCH_LOADER) {
                    String searchReviewsQueryUrl = arg.getString(REVIEWS_SEARCH_QUERY);
                    if (searchReviewsQueryUrl == null || TextUtils.isEmpty(searchReviewsQueryUrl)) {
                        return null;
                    }
                    try {
                        URL reviewUrl = new URL(searchReviewsQueryUrl);
                        return NetworkUtils.getResponseFromHttpUrl(reviewUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                return  null;
            }

            //Cache data
            @Override
            public void deliverResult(String data) {
                int loaderID = getId();
                if (loaderID == VIDEO_SEARCH_LOADER)
                    mVideoJson = data;
                if (loaderID == REVIEWS_SEARCH_LOADER)
                    mReviewJson = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        int loaderID = loader.getId();
        if(loaderID == VIDEO_SEARCH_LOADER) {
            if (data != null && !data.equals("")) {
                movieVideos = new ArrayList<>(boxOfficeVideos.parseJSON(data).movieVideosList);
                parseJsonDataVideo(movieVideos);
            } else {
                showErrorMessage();
            }
        }
        if(loaderID == REVIEWS_SEARCH_LOADER) {
            if (data != null && !data.equals("")) {
                movieReviews = new ArrayList<>(boxOfficeReviews.parseJSON(data).movieReviewsList);
                parseJsonDataReview(movieReviews);

            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private void loadIsFavorite(String id)
    {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(ID_SEARCH_QUERY,id);

        LoaderManager loaderManager = getSupportLoaderManager();

        Loader<Cursor> is_favorite = loaderManager.getLoader(IS_FAVORITE_LOADER);

        if(is_favorite == null)
        {
            loaderManager.initLoader(IS_FAVORITE_LOADER,queryBundle,mLoaderCallbackCursor);
        }
        else
        {
            loaderManager.restartLoader(IS_FAVORITE_LOADER,queryBundle,mLoaderCallbackCursor);
        }
    }




    private LoaderCallbacks<Cursor> mLoaderCallbackCursor = new LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(final int id,final Bundle args) {

            return new AsyncTaskLoader<Cursor>(getApplicationContext()) {

                int loaderID = id;
                // Initialize a Cursor, this will hold all the task data
                Cursor mData = null;

                // onStartLoading() is called when a loader first starts loading data
                @Override
                protected void onStartLoading() {
                    if (mData != null && loaderID == IS_FAVORITE_LOADER) {
                        // Delivers any previously loaded data immediately
                        deliverResult(mData);
                    } else {
                        // Force a new load
                        forceLoad();
                    }
                }

                @Override
                public Cursor loadInBackground() {
                    // Will implement to load data

                    if(loaderID == IS_FAVORITE_LOADER)
                    {
                        String searchId = args.getString(ID_SEARCH_QUERY);
                        final String[] FAVORITE_PROJECTION = {
                                MovieEntry.COLUMN_FAVORITE,
                        };



                        Uri singleUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, Integer.parseInt(searchId));

                        try {
                            return getContentResolver().query(singleUri,
                                    FAVORITE_PROJECTION,
                                    null,
                                    null,
                                    null);

                        } catch (Exception e) {
                            Log.e(TAG, "Failed to asynchronously load data.");
                            e.printStackTrace();
                            return null;
                        }
                    }

                    return null;
                }
                // deliverResult sends the result of the load, a Cursor, to the registered listener
                public void deliverResult(Cursor data) {
                    if(loaderID == IS_FAVORITE_LOADER)
                        mData = data;
                    super.deliverResult(data);
                }
            };
        }


        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

            int loaderID = loader.getId();
            if(loaderID == IS_FAVORITE_LOADER)
            {
                if(cursor.getCount() > 0)
                {
                    int favorite_column = cursor.getColumnIndex(MovieEntry.COLUMN_FAVORITE);

                    cursor.moveToFirst();

                    isFavorite = cursor.getInt(favorite_column) > 0;


                }
                else {


                    isFavorite = false;
                }

                showFavorite(isFavorite);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };
}
