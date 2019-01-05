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

import com.chibusoft.smartcinema.Architecture.MoviesRoom;
import com.chibusoft.smartcinema.Adapters.RoomMovieAdapter.MoviesViewHolder;
import com.chibusoft.smartcinema.R;
import com.squareup.picasso.Picasso;
import java.io.File;


public class RoomMovieAdapter extends PagedListAdapter<MoviesRoom, MoviesViewHolder> {
    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private Context mContext;

    private final RoomMovieAdapter.ItemClickListener mOnCLickListener;

    public interface ItemClickListener{

        void onListItemClick(MoviesRoom movie);
    }

    public RoomMovieAdapter(Context context, RoomMovieAdapter.ItemClickListener listener) {
        super(DIFF_CALLBACK);
        mContext = context;
        mOnCLickListener = listener;
    }


    private static DiffUtil.ItemCallback<MoviesRoom> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MoviesRoom>() {
                @Override
                public boolean areItemsTheSame(MoviesRoom oldItem, MoviesRoom newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(MoviesRoom oldItem, MoviesRoom newItem) {
                    return oldItem.equals(newItem);
                }
            };


    @Override
    public RoomMovieAdapter.MoviesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item_movie;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        RoomMovieAdapter.MoviesViewHolder viewHolder = new RoomMovieAdapter.MoviesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RoomMovieAdapter.MoviesViewHolder holder, int position) {
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
            MoviesRoom movies = getItem(index);

            String path = movies.getPoster_path();

                Picasso.with(mContext)
                        .load(new File(movies.getPoster_path()))
                        .placeholder(R.drawable.loadingmage)
                        .error(R.drawable.errorimage)
                        .into(iconView);

        }

        @Override
        public void onClick(View view)
        {
            //There is no need passing in clicked position with page library
            //instead get the class and pass and instance
            int clickedPosition = getAdapterPosition();
            MoviesRoom movies = getItem(clickedPosition);
            mOnCLickListener.onListItemClick(movies);
        }

    }

}