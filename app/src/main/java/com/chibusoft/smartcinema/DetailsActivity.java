package com.chibusoft.smartcinema;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
import android.widget.Toast;
import com.chibusoft.smartcinema.Adapters.MovieReviewsAdapter;
import com.chibusoft.smartcinema.Adapters.MovieVideosAdapter;
import com.chibusoft.smartcinema.Architecture.AppDatabase;
import com.chibusoft.smartcinema.Architecture.AppExecutors;
import com.chibusoft.smartcinema.Architecture.MovieReviewsRoom;
import com.chibusoft.smartcinema.Architecture.MoviesRoom;
import com.chibusoft.smartcinema.Architecture.MoviesVideoRoom;
import com.chibusoft.smartcinema.Models.MovieReviews;
import com.chibusoft.smartcinema.Models.MovieVideos;
import com.chibusoft.smartcinema.Utilities.RetrofitClientInstance;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailsActivity extends AppCompatActivity  {

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

    private AppDatabase mDb;

    @BindView(R.id.favorite_btn)
    ImageButton favorite_button;

    private boolean isFavorite;

    private String mID;

    private int load_Type;

    private List<MovieVideos.Results> movieVideoList;

    private List<MovieReviews.Results> movieReviewList;

    private ListView listViewVideo;

    private ListView listViewReview;

    public static final String VIDEO_LINK = "https://www.youtube.com/watch?v=";

    private static final String TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        //Initialize member variable for the data base
        mDb = AppDatabase.getInstance(getApplicationContext());

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if(intent.hasExtra(MainActivity.LOAD_TYPE))
            load_Type = intent.getIntExtra(MainActivity.LOAD_TYPE, 1);

        if (intent.hasExtra(MainActivity.ID)) {
            mID = intent.getStringExtra(MainActivity.ID);
        }

        if (intent.hasExtra(MainActivity.TITLE))
            title.setText(intent.getStringExtra(MainActivity.TITLE));

        if (intent.hasExtra(MainActivity.POSTER))
            image_link = intent.getStringExtra(MainActivity.POSTER);

            if (intent.hasExtra(MainActivity.OVERVIEW))
            synopsis.setText(intent.getStringExtra(MainActivity.OVERVIEW));

        if (intent.hasExtra(MainActivity.RELEASE))
            release.setText(intent.getStringExtra(MainActivity.RELEASE));

        if (intent.hasExtra(MainActivity.AVERAGE))
            vote.setText(String.valueOf(intent.getDoubleExtra(MainActivity.AVERAGE, 0)));

        if(load_Type == 1)
        {
            loadIsFavorite(mID);

            RetrofitClientInstance.getInstance()
                    .getApi()
                    .getVideos(Integer.parseInt(mID),MainActivity.KEY)
                    .enqueue(new Callback<MovieVideos>() {
                        @Override
                        public void onResponse(Call<MovieVideos> call, Response<MovieVideos> response) {

                            if(response.body() != null){

                                parseJsonDataVideo(response.body().getResults());
                            }
                        }

                        @Override
                        public void onFailure(Call<MovieVideos> call, Throwable t) {
                            showErrorMessage();
                        }
                    });

            RetrofitClientInstance.getInstance()
                    .getApi()
                    .getReviews(Integer.parseInt(mID),MainActivity.KEY)
                    .enqueue(new Callback<MovieReviews>() {
                        @Override
                        public void onResponse(Call<MovieReviews> call, Response<MovieReviews> response) {

                            if(response.body() != null){

                                parseJsonDataReview(response.body().getResults());
                            }
                        }

                        @Override
                        public void onFailure(Call<MovieReviews> call, Throwable t) {
                            showErrorMessage();
                        }
                    });



            Picasso.with(this)
                        .load(image_link)
                        .placeholder(R.drawable.loadingmage)
                        .error(R.drawable.errorimage)
                        .into(movieimage);

        }
        else if(load_Type == 2)
        {
            loadIsFavorite(mID);
            loadIsFavoriteReview(mID);
            loadIsFavoriteVideo(mID);

            Picasso.with(this)
                    .load(new File(image_link))
                    .placeholder(R.drawable.loadingmage)
                    .error(R.drawable.errorimage)
                    .into(movieimage);
        }

        //Picasso.with(context).load(new File(path)).into(imageView);

        listViewVideo = findViewById(R.id.listview_trailer);

        listViewReview = findViewById(R.id.listview_reviews);

        //isFavorite = false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
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
        isFavorite = isFavorite ? false : true;

        if (isFavorite) {

            //save picture to internal memory
            //Reason i saved to internal memory is because when there is no network and we load from DB picture wont load
            final String[] picturePath = new String[1];
            Picasso.with(this)
                    .load(image_link)
                    .into(new Target() {
                              @Override
                              public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                  try {
                                      File internalStorage = getApplicationContext().getDir("Pictures", Context.MODE_PRIVATE);

                                      if (!internalStorage.exists()) {
                                          internalStorage.mkdirs();
                                      }

                                      File pictureFilePath = new File(internalStorage,
                                              mID + ".jpg");
                                      picturePath[0] = pictureFilePath.toString();

                                      FileOutputStream out = new FileOutputStream(pictureFilePath);
                                      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                      out.flush();
                                      out.close();
                                  } catch(Exception e){
                                      Log.e(TAG, "Problem updating picture", e);
                                  }
                              }


                              @Override
                              public void onBitmapFailed(Drawable errorDrawable) {
                              }

                              @Override
                              public void onPrepareLoad(Drawable placeHolderDrawable) {
                              }
                          }
                    );

            //picture in local storage
            // picturePath[0]


            //Make MoviewRoom final so it is visible inside the run method
            final MoviesRoom moviesRoom = new MoviesRoom(Integer.parseInt(mID), Double.parseDouble(vote.getText().toString()),title.getText().toString(),picturePath[0],
                    synopsis.getText().toString(),release.getText().toString(),isFavorite );



            //We create a list here to model the data we want to insert
            //I Trust they maybe a faster way
            List<MoviesVideoRoom> moviesVideoRoomList = new ArrayList<>();

            if(movieVideoList.size() > 0) {
                for (int i = 0; i < movieVideoList.size(); i++) {

                    moviesVideoRoomList.add(new MoviesVideoRoom(Integer.parseInt(mID),movieVideoList.get(i).getKey(),movieVideoList.get(i).getType()));
                }
            }

            List<MovieReviewsRoom> movieReviewsRoomList = new ArrayList<>();


            if(movieReviewList!= null && movieReviewList.size() > 0) {
                for (int i = 0; i < movieReviewList.size(); i++) {

                    movieReviewsRoomList.add(new MovieReviewsRoom(Integer.parseInt(mID),movieReviewList.get(i).getAuthor(),
                            movieReviewList.get(i).getContent()));
                }
            }

            final List<MovieReviewsRoom> MOVIEREVIEWLIST = movieReviewsRoomList;
            final List<MoviesVideoRoom> MOVIEVIDEOSLIST = moviesVideoRoomList;

            //Here we insert using app executors
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    //Move the remaining logic inside the run method
                    mDb.moviesDao().insertMovies(moviesRoom);
                    mDb.movieVideoDao().insertVideo(MOVIEVIDEOSLIST);
                    mDb.moviesReviewsDao().insertReview(MOVIEREVIEWLIST);
                  //  finish();
                }
            });

            showFavorite(isFavorite);// show favorite

            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

        }
        else
        {
            result = "Removed from Favorites";

            //Delete picture from local storage
            String picturePath = image_link; // See above
            if (picturePath != null && picturePath.length() != 0) {
                File FilePath = new File(picturePath);
                FilePath.delete();
            }

            //Here we insert using app executors
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    //Move the remaining logic inside the run method
                    mDb.moviesDao().DeleteMovieById(Integer.parseInt(mID));
                    mDb.movieVideoDao().DeleteVideoById(Integer.parseInt(mID));
                    mDb.moviesReviewsDao().deleteReviewById(Integer.parseInt(mID));
                 //   finish();
                }
            });

            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }

        showFavorite(isFavorite);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
       else if(id == R.id.action_share)
        {

            if(movieVideoList.size() > 0)
            {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, title.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, VIDEO_LINK + movieVideoList.get(0).getKey());
                startActivity(Intent.createChooser(intent, "Share " + title.getText().toString() + " Trailer"));
            }
            else
            {
                Toast.makeText(this,  title.getText().toString() + " has not trailer to share", Toast.LENGTH_LONG).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    private void parseJsonDataVideo(List<MovieVideos.Results> data)
    {
        movieVideoList = data;

        MovieVideosAdapter movieVideosAdapter = new MovieVideosAdapter(this, movieVideoList);

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
                      String key = movieVideoList.get(position).getKey();
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


    private void parseJsonDataReview(List<MovieReviews.Results> data)
    {
        movieReviewList = data;

        MovieReviewsAdapter movieReviewsAdapter = new MovieReviewsAdapter(this, data);

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

    private void loadIsFavorite(String id)
    {

        LiveData<MoviesRoom> movies = mDb.moviesDao().loadMovieById(Integer.parseInt(id));

        movies.observe(this, new Observer<MoviesRoom>() {
            @Override
            public void onChanged(@Nullable MoviesRoom movieEntries) {
                Log.d(TAG, "Receiving database update from LiveData");
               //Adapter here
                if(movieEntries == null)
                {
                    isFavorite = false;
                    showFavorite(isFavorite);
                }
                else
                {
                    isFavorite = movieEntries.getFavorite();
                    showFavorite(isFavorite);
                }
            }
        });

    }

    private void loadIsFavoriteReview(String id)
    {
        LiveData<List<MovieReviewsRoom>> reviewsList = mDb.moviesReviewsDao().loadReviewById(Integer.parseInt(id));

        reviewsList.observe(this, new Observer<List<MovieReviewsRoom>>() {
            @Override
            public void onChanged(@Nullable List<MovieReviewsRoom> reviewEntries) {
                Log.d(TAG, "Receiving database update from LiveData");
                //Adapter here

               if(movieReviewList == null) movieReviewList = new ArrayList<>();
                movieReviewList.clear();
                MovieReviews movieReviews = new MovieReviews();
                MovieReviews.Results reviews = movieReviews.new Results();

                for(int i = 0; i < reviewEntries.size(); i++)
                {
                    String author = reviewEntries.get(i).getAuthor();
                    String content = reviewEntries.get(i).getContent();

                    //MovieReviews movieReviews = new MovieReviews(author,content);
                    reviews.author = author;
                    reviews.content = content;
                    movieReviewList.add(i,reviews);
                    reviews = movieReviews.new Results();
                }

                parseJsonDataReview(movieReviewList);
            }
        });

    }

    private void loadIsFavoriteVideo(String id)
    {

        LiveData<List<MoviesVideoRoom>> videosList = mDb.movieVideoDao().loadVideoById(Integer.parseInt(id));

        videosList.observe(this, new Observer<List<MoviesVideoRoom>>() {
            @Override
            public void onChanged(@Nullable List<MoviesVideoRoom> videoEntries) {
                Log.d(TAG, "Receiving database update from LiveData");
                //Adapter here


               if(movieVideoList == null) movieVideoList = new ArrayList<>();
                movieVideoList.clear();
                MovieVideos movieVideos = new MovieVideos();
                MovieVideos.Results videos = movieVideos.new Results();

                for(int i = 0; i < videoEntries.size(); i++)
                {
                    String key = videoEntries.get(i).getKey();
                    String type = videoEntries.get(i).getType();

                    videos.setKey(key);// = key;
                    videos.setType(type);// = type;

                     movieVideoList.add(i,videos);
                    videos = movieVideos.new Results();
                }

                parseJsonDataVideo(movieVideoList);
            }
        });

    }

}
