package com.chibusoft.smartcinema;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);


        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

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
}
