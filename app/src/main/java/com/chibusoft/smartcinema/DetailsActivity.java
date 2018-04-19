package com.chibusoft.smartcinema;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.support.v4.app.LoaderManager;

import android.support.v4.content.AsyncTaskLoader;

import android.support.v4.content.Loader;
import android.widget.Toast;

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


public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

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
        if(actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if(intent.hasExtra(MainActivity.ID)) {
            mID = intent.getStringExtra(MainActivity.ID);
            makeReviewSearchQuery(mID);
            makeVideoSearchQuery(mID);
        }

        if(intent.hasExtra(MainActivity.TITLE))
            title.setText(intent.getStringExtra(MainActivity.TITLE));

        if(intent.hasExtra(MainActivity.POSTER))
        {
           String image_link = intent.getStringExtra(MainActivity.POSTER);

            Picasso.with(this)
                    .load(image_link)
                    .placeholder(R.drawable.loadingmage)
                    .error(R.drawable.errorimage)
                    .into(movieimage);
        }

        if(intent.hasExtra(MainActivity.OVERVIEW))
            synopsis.setText(intent.getStringExtra(MainActivity.OVERVIEW));

        if(intent.hasExtra(MainActivity.RELEASE))
            release.setText(intent.getStringExtra(MainActivity.RELEASE));

        if(intent.hasExtra(MainActivity.AVERAGE))
            vote.setText(String.valueOf(intent.getDoubleExtra(MainActivity.AVERAGE, 0)));

        boxOfficeVideos = new BoxOfficeVideos();

        boxOfficeReviews = new BoxOfficeReviews();

        listViewVideo =  (ListView) findViewById(R.id.listview_trailer);

        listViewReview =  (ListView) findViewById(R.id.listview_reviews);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<String> onCreateLoader(int i,final Bundle arg) {
        return new AsyncTaskLoader<String>(this) {

            String mVideoJson, mReviewJson;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(arg == null)
                {
                    return;
                }


                //Cache data
                if (mVideoJson != null || mReviewJson != null) {
                    if(VIDEO_SEARCH_LOADER == getId())
                    deliverResult(mVideoJson);
                    if(REVIEWS_SEARCH_LOADER == getId())
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
                        return NetworkUtils.getResponseFromHttpUrl(videoUrl);
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
                        URL videoUrl = new URL(searchReviewsQueryUrl);
                        return NetworkUtils.getResponseFromHttpUrl(videoUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                return null;
            }

            //Cache data
            @Override
            public void deliverResult(String Json) {
                int loaderID = getId();
                if(loaderID == VIDEO_SEARCH_LOADER)
                    mVideoJson = Json;
            if(loaderID == REVIEWS_SEARCH_LOADER)
                mReviewJson = Json;
                super.deliverResult(Json);
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
        if(loaderID == REVIEWS_SEARCH_LOADER)
        {
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

        Loader<String> videoSearchLoader = loaderManager.getLoader(REVIEWS_SEARCH_LOADER);
        if(videoSearchLoader == null)
        {
            loaderManager.initLoader(REVIEWS_SEARCH_LOADER,queryBundle,this);
        }
        else
        {
            loaderManager.restartLoader(REVIEWS_SEARCH_LOADER,queryBundle,this);
        }
    }




}
