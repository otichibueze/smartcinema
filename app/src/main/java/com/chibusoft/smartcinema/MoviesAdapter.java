package com.chibusoft.smartcinema;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by EBELE PC on 4/1/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>  {


    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private ArrayList<Movies> mMovieList;

    private Context mContext;

    private final ItemClickListener mOnCLickListener;

    public interface ItemClickListener{

        void onListItemClick(int clickedItemIndex);
    }


    public MoviesAdapter(Context context, ArrayList<Movies> movies,ItemClickListener listener) {
        mMovieList = movies;
        mContext = context;
        mOnCLickListener = listener;
    }

//    public MoviesAdapter(Activity context, List<Movies> movies) {
//
//        super(context, 0, movies);
//
//    }


    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item_movie;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
       // Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(mMovieList == null) return  0;

        int mItems = mMovieList.size();

       return mItems;
    }


    class MoviesViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        ImageView iconView;

        public MoviesViewHolder(View itemView) {
            super(itemView);

            //Here we set the items in the number_list_item we created eariler
            //This will also cache the view items
            iconView = itemView.findViewById(R.id.movie_image);

            itemView.setOnClickListener(this);
        }

        void bind(int index) {
            //Set iconView from picasso

            if(MainActivity.load_Type == 1) {
                Picasso.with(mContext)
                        .load(mMovieList.get(index).getmPoster_path())
                        .placeholder(R.drawable.loadingmage)
                        .error(R.drawable.errorimage)
                        .into(iconView);
            }
            else
            {
                Picasso.with(mContext)
                        .load(new File(mMovieList.get(index).getmPoster()))
                        .placeholder(R.drawable.loadingmage)
                        .error(R.drawable.errorimage)
                        .into(iconView);
            }
        }

        @Override
        public void onClick(View view)
        {
            int clickedPosition = getAdapterPosition();
            mOnCLickListener.onListItemClick(clickedPosition);
        }

    }

    public void setData(ArrayList<Movies> movies) {
        mMovieList = movies;
        notifyDataSetChanged();
    }
}

