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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joachimvast.popular_movies_stage2.Database.MoviesDbContract;
import com.example.joachimvast.popular_movies_stage2.Database.MoviesDbHelper;
import com.example.joachimvast.popular_movies_stage2.Main.Movie;
import com.example.joachimvast.popular_movies_stage2.R;
import com.example.joachimvast.popular_movies_stage2.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DetailedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, TrailerAdapter.itemClickListener {

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
    private TrailerAdapter tAdapter;
    private ReviewAdapter rAdapter;
    private TextView mError;
    private Boolean connection;
    private RecyclerView trailerRv;
    private RecyclerView reviewsRv;
    private ArrayList<Trailer> trailers = new ArrayList();
    private ArrayList<Review> reviews = new ArrayList();

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
        mError = (TextView) findViewById(R.id.tv_error_msg);
        trailerRv = (RecyclerView) findViewById(R.id.rv_trailers) ;
        reviewsRv = (RecyclerView) findViewById(R.id.rv_reviews) ;

        // Get the Intent that invoked the start of this Activity
        Intent intent = getIntent();

        // Retrieve the id from our intent
        idMovie = intent.getStringExtra("id");

        // Create a LayoutManager for the RecyclerView
        LinearLayoutManager m1 = new LinearLayoutManager(this);
        LinearLayoutManager m2 = new LinearLayoutManager(this);

        // Set the LayoutManager for the RecyclerView object
        trailerRv.setLayoutManager(m1);
        trailerRv.hasFixedSize();

        reviewsRv.setLayoutManager(m2);
        reviewsRv.hasFixedSize();

        connection = NetworkUtils.isOnline(this);

        // Get the Database
        dbhelper = new MoviesDbHelper(this);
        db = dbhelper.getWritableDatabase();

        tAdapter = new TrailerAdapter(this);
        // Set our adapter
        trailerRv.setAdapter(tAdapter);

        rAdapter = new ReviewAdapter();
        // Set our adapter
        reviewsRv.setAdapter(rAdapter);

        // run the loader
        runLoader();

        // fetch trailers and reviews
        fetchExtended();
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


    public void fetchExtended() {
        // Get our URL, pass on the sort variable
        URL trailersURL = NetworkUtils.extendedURL(idMovie, "videos");
        URL reviewsURL = NetworkUtils.extendedURL(idMovie, "reviews");

        // Make a new MovieQueryTask Object and execute the task
        new FetchTask().execute(trailersURL, reviewsURL);
    }

    private void displayData() {

        mCursor.moveToFirst();
        favourized = mCursor.getInt(mCursor.getColumnIndex(MoviesDbContract.MovieEntry.COLUMN_NAME_FAVOURITES));

        if(favourized == 1) {
            mButton.setText("Unmark as favourite");
        } else {
            mButton.setText("Mark as favourite");
        }
        // Set the texts of our TextViews to the output
        mSynopsis.setText("Synopsis :" + "\n" + mCursor.getString(mCursor.getColumnIndex(MoviesDbContract.MovieEntry.COLUMN_NAME_OVERVIEW)));
        mTitle.setText(mCursor.getString(mCursor.getColumnIndex(MoviesDbContract.MovieEntry.COLUMN_NAME_TITLE)));

        mRelease.setText(mCursor.getString(mCursor.getColumnIndex(MoviesDbContract.MovieEntry.COLUMN_NAME_RELEASE)));
        mRating.setText(mCursor.getString(mCursor.getColumnIndex(MoviesDbContract.MovieEntry.COLUMN_NAME_RATING)));

        // Create an image and place it in our ImageView variable
        Picasso.with(mThumbnail.getContext())
                .load(mCursor.getString(mCursor.getColumnIndex(MoviesDbContract.MovieEntry.COLUMN_NAME_THUMBNAIL)))
                .into(mThumbnail);

        mError.setVisibility(View.INVISIBLE);
    }

    private void displayError() {
        mError.setText("Error fetching reviews and trailers, make sure your internet connection is online");
        mError.setVisibility(View.VISIBLE);
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

        if(favourized == 1) {
            cv.put(MoviesDbContract.MovieEntry.COLUMN_NAME_FAVOURITES, 0);
            db.update(MoviesDbContract.MovieEntry.TABLE_NAME,
                    cv,
                    MoviesDbContract.MovieEntry.COLUMN_NAME_ID + "= ?",
                    new String[] {idMovie}
            );
            mButton.setText("Mark as favourite");
            Toast mToast = Toast.makeText(this, "Unmarked as favourite", Toast.LENGTH_LONG);
            mToast.show();
        }

        else {
            cv.put(MoviesDbContract.MovieEntry.COLUMN_NAME_FAVOURITES, 1);
            db.update(MoviesDbContract.MovieEntry.TABLE_NAME,
                    cv,
                    MoviesDbContract.MovieEntry.COLUMN_NAME_ID + "= ?",
                    new String[] {idMovie}
            );
            mButton.setText("Unmark as favourite");
            Toast mToast = Toast.makeText(this, "Marked as favourite", Toast.LENGTH_LONG);
            mToast.show();
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
                     return  getContentResolver().query(MoviesDbContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(idMovie).build(),
                                null,
                                null,
                                null,
                                null);

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

    @Override
    public void onTrailerClick(int clickedItemIndex) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailers.get(clickedItemIndex).getTrailerURL())));
    }

    public class FetchTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected String[] doInBackground(URL... params) {
            // Get the URL
            URL trailers = params[0];
            URL reviews = params[1];

            // Initiate results String
            String resultsTrailers = null;
            String resultsreviews = null;

            // try - catch to catch any IOExceptions
            try {
                // Set the value of the results String to the response from the HTTP request
                resultsTrailers = NetworkUtils.getResponseFromHttpUrl(trailers);
                resultsreviews = NetworkUtils.getResponseFromHttpUrl(reviews);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new String[]{resultsTrailers, resultsreviews};
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] results) {
            if (!connection) {
                displayError();
            } else {

                // If the results from our HTTP request are not null, display the data
                if (results != null && !results.equals("")) {

                    // Parse our JSONString
                    try {
                        // Make an object of our JSON String
                        JSONObject objectTrailers = new JSONObject(results[0]);
                        JSONObject objectReviews = new JSONObject(results[1]);

                        // Make an array of our JSON Object
                        JSONArray arrayTrailers = objectTrailers.getJSONArray("results");
                        JSONArray arrayReviews = objectReviews.getJSONArray("results");

                        // Iterate over each JSONObject and add them to our ArrayList<Movie> variable
                        for (int i = 0; i < arrayTrailers.length(); i++) {

                            // Create a movie object with the index of the array
                            Trailer trailer = new Trailer(arrayTrailers.getJSONObject(i));
                            trailers.add(trailer);
                        }

                        // Iterate over each JSONObject and add them to our ArrayList<Movie> variable
                        for (int i = 0; i < arrayReviews.length(); i++) {

                            // Create a movie object with the index of the array
                            Review review = new Review(arrayReviews.getJSONObject(i));
                            reviews.add(review);
                        }

                        rAdapter.setList(reviews);
                        tAdapter.setList(trailers);

                        rAdapter.notifyDataSetChanged();
                        tAdapter.notifyDataSetChanged();

                        Log.d("Trailers", trailers.toString());
                        Log.d("Reviews", reviews.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Display error
                    displayError();
                }
            }
        }
    }
}
