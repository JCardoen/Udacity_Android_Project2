package com.example.joachimvast.popular_movies_stage2.Detailed;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joachimvast.popular_movies_stage2.Database.MoviesDbHelper;
import com.example.joachimvast.popular_movies_stage2.R;
import com.example.joachimvast.popular_movies_stage2.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DetailedActivity extends AppCompatActivity {

    // Declare variables
    private ImageView mThumbnail;
    private TextView mSynopsis;
    private TextView mTitle;
    private TextView mRelease;
    private TextView mRating;
    private Button mButton;
    private SQLiteDatabase db;
    private MoviesDbHelper dbhelper;
    private final String BASE_URL = "https://www.youtube.com/watch?v=";
    private ArrayList<String> trailerKeys = new ArrayList();
    private ArrayList<String> reviewsContent =  new ArrayList();
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: create a new AsyncTask combined with a new RecyclerView and Adapter
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        // Set the content view and store the actionbar as a variable
        ActionBar actionBar = this.getSupportActionBar();

        // If the action bar is not null, we display the back to home button (=> MainActivity)
        if (actionBar != null) {
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }

        // Reference to the ID of the corresponding view
        mThumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
        mSynopsis = (TextView) findViewById(R.id.tv_synopsis);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mRelease = (TextView) findViewById(R.id.tv_release_date);
        mRating = (TextView) findViewById(R.id.tv_rating);
        mButton = (Button) findViewById(R.id.button_favourize);

        // Get the Intent that invoked the start of this Activity
        Intent intent = getIntent();

        // Set the texts of our TextViews to the output
        mSynopsis.setText("Synopsis :" + "\n" + intent.getStringExtra("overview"));
        mTitle.setText(intent.getStringExtra("title"));
        //mRelease.setText(intent.getStringExtra("release").substring(0, 4));
        mRating.setText(intent.getStringExtra("rating"));

        // Create an image and place it in our ImageView variable
        Picasso.with(mThumbnail.getContext())
                .load(intent.getStringExtra("image_path"))
                .into(mThumbnail);

        // Retrieve the id from our intent
        id = intent.getStringExtra("id");

        dbhelper = new MoviesDbHelper(this);
        db = dbhelper.getWritableDatabase();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }



//TODO: update code to use ContentProvider and specified ID
    public void markAsFavourite(View view) {
        /*if(mButton.getText() == getString(R.string.add_to_favourites)) {
            dbhelper.markFavourite(id, db);
            mButton.setText(getString(R.string.added_to_favourites));
            mButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else {
            dbhelper.unmarkFavourite(id, db);
            mButton.setText(getString(R.string.add_to_favourites));
            mButton.setBackgroundColor(getResources().getColor(R.color.mainColor));
        }*/
    }
}
