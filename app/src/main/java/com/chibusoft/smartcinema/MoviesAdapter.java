package com.chibusoft.smartcinema;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by EBELE PC on 4/1/2018.
 */

public class MoviesAdapter extends ArrayAdapter<Movies> {

    public MoviesAdapter(Activity context, List<Movies> movies) {

        super(context, 0, movies);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Movies movies = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);
        }

        //TextView text = (TextView) convertView.findViewById(R.id.test);
        //text.setText(movies.getmTitle());

        ImageView iconView = convertView.findViewById(R.id.movie_image);
       // iconView.setImageResource(androidFlavor.image);

        //Set iconView from picasso
        Picasso.with(getContext())
                .load(movies.getmPoster_path())
                .placeholder(R.drawable.loadingmage)
                .error(R.drawable.errorimage)
                .into(iconView);


        return convertView;
    }
}

