package com.example.joachimvast.popular_movies_stage1;

import android.app.Application;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by JoachimVAST on 06/02/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>  {

    // Declare variables
    private ArrayList<Movie> mList = new ArrayList<>();
    private itemClickListener clickListener;

    // Create an interface for our clickListener
    public interface itemClickListener {
        void onItemClick(int clickedItemIndex);
    }

    // Change the constructor of MovieAdapter to accept a onClicklistener
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
        // Get a Movie object from the ArrayList<Movie> from our Adapter class
        Movie movie = mList.get(position);

        // Get the imagePath of this object
        String thumbnailURL = movie.imagePath;

        // Use picasso to store the image inside the ImageView
        Picasso.with(holder.mThumbnail.getContext())
                .load(thumbnailURL)
                .into(holder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
