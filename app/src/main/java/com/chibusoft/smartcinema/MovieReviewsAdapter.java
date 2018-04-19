package com.chibusoft.smartcinema;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by EBELE PC on 4/19/2018.
 */

public class MovieReviewsAdapter extends ArrayAdapter<MovieReviews> {

    private static final String LOG_TAG = MovieReviewsAdapter.class.getSimpleName();

    public MovieReviewsAdapter(Activity context, List<MovieReviews> movieReviews) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movieReviews);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        MovieReviews movieReviews = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_review_item, null);
        }


        TextView authorItem = (TextView) convertView.findViewById(R.id.author_text);
        authorItem.setText(movieReviews.getAuthor());


        TextView commentItem = (TextView) convertView.findViewById(R.id.comment_text);
        commentItem.setText(movieReviews.getContent());

        return convertView;
    }


}
