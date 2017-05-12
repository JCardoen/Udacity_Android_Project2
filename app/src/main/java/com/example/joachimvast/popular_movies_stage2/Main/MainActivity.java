package com.example.joachimvast.popular_movies_stage2.Main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.joachimvast.popular_movies_stage2.Database.MoviesDbContract;
import com.example.joachimvast.popular_movies_stage2.Database.MoviesDbHelper;
import com.example.joachimvast.popular_movies_stage2.Detailed.DetailedActivity;
import com.example.joachimvast.popular_movies_stage2.R;
import com.example.joachimvast.popular_movies_stage2.Settings.SettingsActivity;
import com.example.joachimvast.popular_movies_stage2.Utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.itemClickListener, SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Cursor> {

    // Declare variables (private)
    private TextView mError;
    private ScrollView mScrollview;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ArrayList<Movie> movielist = new ArrayList<>();
    public String sort = "";
    private Boolean connection;
    private SQLiteDatabase db;
    private MoviesDbHelper dbhelper;
    Cursor mCursor;

    // Constant int for the our LoaderManager
    private final int LOADER_ID = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference ID to each variable
        mError = (TextView) findViewById(R.id.tv_error_msg);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_thumb);
        mScrollview = (ScrollView) findViewById(R.id.sv);

        // Create a LayoutManager for the RecyclerView
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        // Set the LayoutManager for the RecyclerView object
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.hasFixedSize();

        // Initialize loadermanager
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        // Setup Shared preferences
        setupSharedPreferences();

        // Boolean to check whether or not we are connected to the internet
        connection = isOnline();

        // Get a reference to our writable database
        dbhelper = new MoviesDbHelper(this);
        db = dbhelper.getWritableDatabase();

        // Run synchronization
        runSync();

        Log.d("DEBUG SORTING", sort);
        Log.d("String", getString(R.string.favourites_key));

        mAdapter = new MovieAdapter(this);
        // Set our adapter
        mRecyclerView.setAdapter(mAdapter);

        makeQuery();
    }

    public void runSync() {
        // Get our URL, pass on the sort variable
        URL popularUrl = NetworkUtils.buildUrl("popular");
        URL topUrl = NetworkUtils.buildUrl("top_rated");

        // Make a new MovieQueryTask Object and execute the task
        new MovieSyncTask().execute(popularUrl, topUrl);
    }

    // Method to check whether or not the user is connected to the internet
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    // Method for handling click on a thumbnail
    @Override
    public void onItemClick(int clickedItemIndex) {

        // Get the parent context
        Context context = MainActivity.this;

        // Get the class of destination activity
        Class destinationActivity = DetailedActivity.class;

        // Make a new intent
        Intent intent = new Intent(context, destinationActivity);

        //TODO: Use the ID with ContentProvider to display detailed view.

        // Start the activity using the intent
        startActivity(intent);
    }

    // Make a query, called when the Search button is clicked
    public void makeQuery() {

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

    // Display an error
    public void displayError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mError.setText(R.string.error_msg);
        mError.setVisibility(View.VISIBLE);
    }

    // Display our data
    public void showData() {
        mError.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

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
                String[] columns = {MoviesDbContract.MovieEntry.COLUMN_NAME_THUMBNAIL};
                String[] selectionSort = {sort};
                String[] selectionFavourites = {"1"};
                Log.d("Sorting:", sort);
                Cursor c = null;
                Cursor convertToMovieList;
                try {
                    if (sort.equals(getString(R.string.favourites_key))) {
                        return getContentResolver().query(MoviesDbContract.MovieEntry.CONTENT_URI,
                                columns,
                                MoviesDbContract.MovieEntry.COLUMN_NAME_FAVOURITES + "= ?",
                                selectionFavourites,
                                MoviesDbContract.MovieEntry.COLUMN_NAME_THUMBNAIL);
                    } else {
                        return getContentResolver().query(MoviesDbContract.MovieEntry.CONTENT_URI,
                                columns,
                                MoviesDbContract.MovieEntry.COLUMN_NAME_SORTING + "= ?",
                                selectionSort,
                                MoviesDbContract.MovieEntry.COLUMN_NAME_THUMBNAIL);
                    }

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
        if (data == null) {
            displayError();
        } else {
            // Swap cursor
            mAdapter.swapCursor(data);
            Log.d("Cursor:", DatabaseUtils.dumpCursorToString(data));
            // Show the JSON data
            showData();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    // Helper method to setup the sharedPreferences
    private void setupSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadSortFromPreferences(preferences);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }


    // On changed preference we call the helper method
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sort_key))) {
            loadSortFromPreferences(sharedPreferences);

            // We clear arraylist
            movielist.clear();

            // Make a new Query to populate our arraylist object that stores the movies
            makeQuery();

            // re-populate the recyclerviews using the notifier method on our adapter
            this.mAdapter.notifyDataSetChanged();
        }
    }


    // Helper method to set our sort variable
    private void loadSortFromPreferences(SharedPreferences sharedPreferences) {
        this.sort = sharedPreferences.getString(getString(R.string.sort_key), getString(R.string.popular_key));
    }

    // Unregister preference listener
    @Override
    protected void onDestroy() {

        // We close the Movies db
        dbhelper.close();
        super.onDestroy();

        // Unregistering the PreferenceChangeListener
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Restart the LoaderManager
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    // Create the Settingsmenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.preferences, menu);
        return true;
    }

    // If the settings option was selected, open that activity using an intent
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sorting_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class MovieSyncTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected String[] doInBackground(URL... params) {
            // Get the URL
            URL popular = params[0];
            URL top = params[1];

            // Initiate results String
            String resultsPopular = null;
            String resultsTop = null;

            // try - catch to catch any IOExceptions
            try {
                // Set the value of the results String to the response from the HTTP request
                resultsPopular = NetworkUtils.getResponseFromHttpUrl(popular);
                resultsTop = NetworkUtils.getResponseFromHttpUrl(top);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new String[]{resultsPopular, resultsTop};
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
                        JSONObject objectPop = new JSONObject(results[0]);
                        JSONObject objectTop = new JSONObject(results[1]);

                        // Make an array of our JSON Object
                        JSONArray arrayPop = objectPop.getJSONArray("results");
                        JSONArray arrayTop = objectTop.getJSONArray("results");

                        // Iterate over each JSONObject and add them to our ArrayList<Movie> variable
                        for (int i = 0; i < arrayPop.length(); i++) {

                            // Create a movie object with the index of the array
                            Movie movie = new Movie(arrayPop.getJSONObject(i));
                            Log.d("MyActivity", movie.imagePath);

                            dbhelper.insertMovie(movie, db, "popular");
                        }

                        // Iterate over each JSONObject and add them to our ArrayList<Movie> variable
                        for (int i = 0; i < arrayTop.length(); i++) {

                            // Create a movie object with the index of the array
                            Movie movie = new Movie(arrayTop.getJSONObject(i));
                            Log.d("MyActivity", movie.imagePath);
                            dbhelper.cleanDb(db);
                            dbhelper.insertMovie(movie, db, "top_rated");
                        }

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
