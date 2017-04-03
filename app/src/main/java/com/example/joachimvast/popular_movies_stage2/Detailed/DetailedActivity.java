package com.example.joachimvast.popular_movies_stage2.Detailed;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joachimvast.popular_movies_stage2.R;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class DetailedActivity extends AppCompatActivity  {

    // Declare variables
    ImageView mThumbnail;
    TextView mSynopsis;
    TextView mTitle;
    TextView mRelease;
    TextView mRating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        ActionBar actionBar = this.getSupportActionBar();

        if(actionBar != null){
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }

        // Reference to the ID of the corresponding view
        mThumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
        mSynopsis = (TextView) findViewById(R.id.tv_synopsis);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mRelease = (TextView) findViewById(R.id.tv_release_date);
        mRating = (TextView) findViewById(R.id.tv_rating);


        // Get the Intent that invoked the start of this Activity
        Intent intent = getIntent();

        // Set the text of our TextView to the output
        mSynopsis.setText(intent.getStringExtra("overview"));
        mTitle.setText(intent.getStringExtra("title"));
        mRelease.setText(intent.getStringExtra("release"));
        mRating.setText(intent.getStringExtra("rating"));

        // Create an image and place it in our ImageView variable
        Picasso.with(mThumbnail.getContext())
                .load(intent.getStringExtra("image_path"))
                .into(mThumbnail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    // New TrailerTast to fetch the trailers
    public class TrailerTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
