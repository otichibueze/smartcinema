package com.chibusoft.smartcinema;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by EBELE PC on 4/18/2018.
 */

public class MovieVideosAdapter extends ArrayAdapter<MovieVideos> {

    private static final String LOG_TAG = MovieVideosAdapter.class.getSimpleName();

    public MovieVideosAdapter(Activity context, List<MovieVideos> movieVideos) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movieVideos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        MovieVideos movieVideo = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_trailer_item, null);
        }



        TextView trailerItem = convertView.findViewById(R.id.trailer_text);
        int num = position + 1;
        trailerItem.setText(movieVideo.type + "  " + num);


        return convertView;


    }


}
