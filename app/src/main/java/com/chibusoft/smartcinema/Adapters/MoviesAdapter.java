package com.chibusoft.smartcinema.Adapters;


import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.chibusoft.smartcinema.Adapters.MoviesAdapter.MoviesViewHolder;
import com.chibusoft.smartcinema.MainActivity;
import com.chibusoft.smartcinema.Models.Movies.Results;
import com.chibusoft.smartcinema.R;
import com.squareup.picasso.Picasso;
import java.io.File;



/**
 * Created by EBELE PC on 4/1/2018.
 */

public class MoviesAdapter extends PagedListAdapter<Results, MoviesViewHolder> {


    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private Context mContext;

    private final ItemClickListener mOnCLickListener;

    public interface ItemClickListener{

        void onListItemClick(Results movie);
    }

        //ArrayList<Movies> movies,
        public MoviesAdapter(Context context, ItemClickListener listener) {
        super(DIFF_CALLBACK);
       // mMovieList = movies;
        mContext = context;
        mOnCLickListener = listener;
    }


    private static DiffUtil.ItemCallback<Results> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Results>() {
                @Override
                public boolean areItemsTheSame(Results oldItem, Results newItem) {
                    return oldItem.getmId() == newItem.getmId();
                }

                @Override
                public boolean areContentsTheSame(Results oldItem, Results newItem) {
                    return oldItem.equals(newItem);
                }
            };


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
            Results movies = getItem(index);

            if(MainActivity.load_Type == 1) {
                Picasso.with(mContext)
                        .load(movies.getmPoster_path())
                        .placeholder(R.drawable.loadingmage)
                        .error(R.drawable.errorimage)
                        .into(iconView);
            }
            else
            {
                Picasso.with(mContext)
                        .load(new File(movies.getmPoster()))
                        .placeholder(R.drawable.loadingmage)
                        .error(R.drawable.errorimage)
                        .into(iconView);
            }
        }

        @Override
        public void onClick(View view)
        {
            //There is no need passing in clicked position with page library
            //instead get the class and pass and instance
            int clickedPosition = getAdapterPosition();
            Results movies = getItem(clickedPosition);
            mOnCLickListener.onListItemClick(movies);
        }

    }

}

