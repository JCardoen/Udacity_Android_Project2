package com.example.joachimvast.popular_movies_stage2.Detailed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joachimvast.popular_movies_stage2.R;

import java.util.ArrayList;

/**
 * Created by Joachim on 14/05/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private itemClickListener clickListener;
    private ArrayList<Trailer> trailers = new ArrayList();

    // Create an interface for our clickListener
    public interface itemClickListener {
        void onTrailerClick(int clickedItemIndex);
    }

    public void setList(ArrayList<Trailer> list) {
        this.trailers = list;
    }

    public TrailerAdapter(itemClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.trailersrecyclerview;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.mTrailers.setText(trailers.get(position).toString());
    }

    @Override
    public int getItemCount() {
        int size = (trailers.size() == 0) ?  0 :  trailers.size() ;
        return size;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTrailers;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            // Reference ImageView to ID
            this.mTrailers = (TextView) itemView.findViewById(R.id.rv_trailers);

            // Set the onclick listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickListener.onTrailerClick(position);
        }
    }
}
