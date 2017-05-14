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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList<Review> reviews = new ArrayList();

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.reviewsrecyclerview;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        return new ReviewViewHolder(view);
    }

    public void setList(ArrayList<Review> list) {
        this.reviews = list;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.mReviews.setText(reviews.get(position).toString());
    }

    @Override
    public int getItemCount() {
        int size = (reviews.size() == 0) ?  0 :  reviews.size() ;
        return size;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        public TextView mReviews;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            // Reference textView to ID
            this.mReviews = (TextView) itemView.findViewById(R.id.rv_reviews);
        }
    }
}
