package com.example.joachimvast.popular_movies_stage2;

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

import com.squareup.picasso.Picasso;

public class Detailed extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    // Declare variables
    ImageView mThumbnail;
    TextView mOverview;



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
        mOverview = (TextView) findViewById(R.id.tv_overview);

        // Get the Intent that invoked the start of this Activity
        Intent intent = getIntent();

        // Declare a variable for the output in our TextView
        String output = "";

        // Get all the data sent via the Intent
        output += "Title: " + intent.getStringExtra("title") + "\n";
        output += "Release date: " + intent.getStringExtra("release") + "\n";
        output += "Rating (out of 10): " + intent.getStringExtra("rating") + "\n";
        output += "Overview: " + intent.getStringExtra("overview")+ "\n";

        // Set the text of our TextView to the output
        mOverview.setText(output);

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

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
