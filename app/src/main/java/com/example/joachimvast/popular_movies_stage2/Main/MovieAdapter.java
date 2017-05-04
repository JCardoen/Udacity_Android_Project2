package com.example.joachimvast.popular_movies_stage2.Main;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.joachimvast.popular_movies_stage2.Database.DBContract;

import com.example.joachimvast.popular_movies_stage2.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by JoachimVAST on 06/02/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>  {

    // Declare variables
    private ArrayList<Movie> mList = new ArrayList<>();
    private itemClickListener clickListener;
    private Cursor mCursor;

    // Create an interface for our clickListener
    public interface itemClickListener {
        void onItemClick(int clickedItemIndex);
    }

    // Change the constructor of MovieAdapter to accept an onClicklistener and a Cursor object
    public MovieAdapter(itemClickListener listener, Cursor cursor){
        clickListener = listener;
        mCursor = cursor;
    }

    // Change the constructor of MovieAdapter to accept an onClicklistener
    public MovieAdapter(itemClickListener listener){
        clickListener = listener;
    }

    // Setter for the ArrayList of movie objects
    public void setList(ArrayList<Movie> movielist){
        this.mList = movielist;

        // Notify android engine that the data has changed
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Create an ImageView for Picasso to store the Image
        public ImageView mThumbnail;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);

            // Reference ImageView to ID
            this.mThumbnail = (ImageView) itemView.findViewById(R.id.iv_thumb);

            // Set the onclick listener
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            // Get current position from adapter
            int position = getAdapterPosition();

            // Pass it onto onItemClick() in itemClickListener interface
            clickListener.onItemClick(position);
        }
    }

    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.gridrecycleview;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId,parent,shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieAdapterViewHolder holder, int position) {
        String thumbnailURL ="";

        /* If Cursor is not null , this means that the application fetches the thumbnails from the DB
        /** We use Picasso's caching to display the thumbnail
        /** Picasso has default caching enabled and we should just pass in the URL
        */

        if(mCursor != null) {
            if(!mCursor.moveToPosition(position))
                return;

            // Fetch the thumbnail from our cursor object
            thumbnailURL = mCursor.getString(mCursor.getColumnIndex("thumbnail"));
            Log.d("Debug", thumbnailURL);

        } else {

            // Get a Movie object from the ArrayList<Movie> from our Adapter class
            Movie movie = mList.get(position);
            // Get the imagePath of this object
            thumbnailURL = movie.imagePath;
        }

        // Use picasso to store the image inside the ImageView
        Picasso.with(holder.mThumbnail.getContext())
                .load(thumbnailURL)
                .into(holder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) {
            return mList.size();
        }
        return mCursor.getCount();
    }
}
