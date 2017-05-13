package com.example.joachimvast.popular_movies_stage2.Main;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.joachimvast.popular_movies_stage2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by JoachimVAST on 06/02/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private itemClickListener clickListener;
    private Cursor mCursor;

    public Cursor getCursor() {
        return mCursor;
    }

    // Create an interface for our clickListener
    public interface itemClickListener {
        void onItemClick(int clickedItemIndex);
    }

    // Change the constructor of MovieAdapter to accept an onClicklistener and a Cursor object
    public MovieAdapter(itemClickListener listener, Cursor cursor) {
        clickListener = listener;
        mCursor = cursor;
    }

    // Change the constructor of MovieAdapter to accept an onClicklistener
    public MovieAdapter(itemClickListener listener) {
        clickListener = listener;
    }


    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieAdapterViewHolder holder, int position) {

        /* If Cursor is not null , this means that the application fetches the thumbnails from the DB
        /** We use Picasso's caching to display the thumbnail
        /** Picasso has default caching enabled and we should just pass in the URL
        */

        mCursor.moveToPosition(position);

        // Fetch the thumbnail from our cursor object
        String thumbnailURL = mCursor.getString(mCursor.getColumnIndex("thumbnail"));
        Log.d("Debug", thumbnailURL);

        // Use picasso to store the image inside the ImageView
        Picasso.with(holder.mThumbnail.getContext())
                .load(thumbnailURL)
                .into(holder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
}
