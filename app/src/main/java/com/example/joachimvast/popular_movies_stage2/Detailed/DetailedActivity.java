package com.example.joachimvast.popular_movies_stage2.Detailed;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joachimvast.popular_movies_stage2.Database.MoviesDbContract;
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

public class DetailedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Declare variables
    private ImageView mThumbnail;
    private TextView mSynopsis;
    private TextView mTitle;
    private TextView mRelease;
    private TextView mRating;
    private Button mButton;
    private int favourized;
    private SQLiteDatabase db;
    private MoviesDbHelper dbhelper;
    private String idMovie;
    private Cursor mCursor;

    private static final int LOADER_ID = 78;

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

        // Retrieve the id from our intent
        idMovie = intent.getStringExtra("id");

        // Get the Database
        dbhelper = new MoviesDbHelper(this);
        db = dbhelper.getWritableDatabase();

        // run the loader
        runLoader();
    }

    private void runLoader() {
        // Get loadermanager as a variable
        LoaderManager ldmanager = getSupportLoaderManager();

        // Get Loader and store in a variable
        Loader<String> loader = ldmanager.getLoader(LOADER_ID);

        // If loader is not yet initialize, initialize it
        if (loader == null) {
            ldmanager.initLoader(LOADER_ID, null, this);
        }
        // Restart if it is already initialized
        else {
            ldmanager.restartLoader(LOADER_ID, null, this);
        }
    }

    private void displayData() {

        mCursor.moveToFirst();
        favourized = mCursor.getInt(mCursor.getColumnIndex(MoviesDbContract.MovieEntry.COLUMN_NAME_FAVOURITES));
        // Set the texts of our TextViews to the output
        mSynopsis.setText("Synopsis :" + "\n" + mCursor.getString(mCursor.getColumnIndex(MoviesDbContract.MovieEntry.COLUMN_NAME_OVERVIEW)));
        mTitle.setText(mCursor.getString(mCursor.getColumnIndex(MoviesDbContract.MovieEntry.COLUMN_NAME_TITLE)));

        mRelease.setText(mCursor.getString(mCursor.getColumnIndex(MoviesDbContract.MovieEntry.COLUMN_NAME_RELEASE)));
        mRating.setText(mCursor.getString(mCursor.getColumnIndex(MoviesDbContract.MovieEntry.COLUMN_NAME_RATING)));

        // Create an image and place it in our ImageView variable
        Picasso.with(mThumbnail.getContext())
                .load(mCursor.getString(mCursor.getColumnIndex(MoviesDbContract.MovieEntry.COLUMN_NAME_THUMBNAIL)))
                .into(mThumbnail);
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
        ContentValues cv = new ContentValues();
        Toast mToast = new Toast(this);
        if(favourized == 1) {
            cv.put(MoviesDbContract.MovieEntry.COLUMN_NAME_FAVOURITES, 0);
            db.update(MoviesDbContract.MovieEntry.TABLE_NAME,
                    cv,
                    MoviesDbContract.MovieEntry.COLUMN_NAME_ID + "= ?",
                    new String[] {idMovie}
            );
        }

        else {
            cv.put(MoviesDbContract.MovieEntry.COLUMN_NAME_FAVOURITES, 1);
            db.update(MoviesDbContract.MovieEntry.TABLE_NAME,
                    cv,
                    MoviesDbContract.MovieEntry.COLUMN_NAME_ID + "= ?",
                    new String[] {idMovie}
            );
        }

    }

    // Loader to get a Cursor from our Database containing the information of the movies based on sorting
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Variable to store raw JSON data in
            Cursor movies;

            @Override
            protected void onStartLoading() {

                // When we have a cached Cursor object, deliverResult
                if (movies != null) {
                    deliverResult(movies);
                } else {
                    forceLoad();
                }

                super.onStartLoading();
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    Uri uri = MoviesDbContract.MovieEntry.CONTENT_URI.buildUpon().build();
                     return  getContentResolver().query(uri,
                                null,
                                MoviesDbContract.MovieEntry.COLUMN_NAME_ID + "= ?",
                                new String[] {idMovie},
                                MoviesDbContract.MovieEntry.COLUMN_NAME_THUMBNAIL);

                } catch (Exception e) {
                    Log.e("TAG :", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                movies = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        Log.d("ClickedPos:", DatabaseUtils.dumpCursorToString(mCursor));
        // Display data
        displayData();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getSupportLoaderManager().restartLoader(LOADER_ID,null,this);
    }
}
