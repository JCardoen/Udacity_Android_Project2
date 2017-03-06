package com.example.joachimvast.popular_movies_stage2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.itemClickListener, SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<String> {

    // Declare variables
    TextView mError;
    ScrollView mScrollview;
    RecyclerView mRecyclerView;
    MovieAdapter mAdapter;
    Spinner mSorting;
    ArrayList<Movie> movielist = new ArrayList<Movie>();
    String sort = "";
    String JSON_MOVIES;
    String API_MOVIE_URL;
    Boolean connection;
    ArrayAdapter<String> sAdapter;

    // Constant int for the our LoaderManager
    private final int LOADER_ID = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference ID to each variable
        mError = (TextView) findViewById(R.id.tv_error_msg);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_thumb);
        mScrollview = (ScrollView) findViewById(R.id.sv) ;

        // Create a LayoutManager for the RecyclerView
        GridLayoutManager manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);

        // Set the LayoutManager for the RecyclerView object
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.hasFixedSize();

        // Make a new MovieAdapter object and set the adapter of the RecyclerView to that object
        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        // Initialize loadermanager
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        setupSharedPreferences();
        makeQuery();
        connection = isOnline();
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
        Class destinationActivity = Detailed.class;

        // Make a new intent
        Intent intent = new Intent(context, destinationActivity);

        // Get selected Movie Object to append data to our Intent so the ChildActivity can parse this into a TextView
        Movie selected = movielist.get(clickedItemIndex);
        intent.putExtra("image_path", selected.imagePath);
        intent.putExtra("title", selected.title);
        intent.putExtra("rating", selected.rating);
        intent.putExtra("release", selected.release);
        intent.putExtra("overview", selected.overview);

        // Start the activity using the intent
        startActivity(intent);
    }

    // Make a query, called when the Search button is clicked
    public void makeQuery() {

        Bundle queryBundle = new Bundle();

        // Get our URL, pass on the sort variable
        URL apiUrl = NetworkUtils.buildUrl(this.sort);

        queryBundle.putString(API_MOVIE_URL, apiUrl.toString());


        LoaderManager ldmanager = getSupportLoaderManager();
        Loader<String> loader = ldmanager.getLoader(LOADER_ID);

        if(loader == null) {
            ldmanager.initLoader(LOADER_ID,queryBundle,this);
        } else {
            ldmanager.restartLoader(LOADER_ID,queryBundle,this);
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
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            // Variable to store raw JSON data in
            String movieJSON;

            @Override
            protected void onStartLoading() {
                if(args == null) {
                    return;
                }

                if(movieJSON != null) {
                    deliverResult(movieJSON);
                }
                else {
                    forceLoad();
                }

                super.onStartLoading();
            }

            @Override
            public String loadInBackground() {
                String moviesString = args.getString(API_MOVIE_URL);

                if(moviesString == null) {
                    return null;
                }

                String results = "";

                // try - catch to catch any IOExceptions
                try{
                    URL moviesURL = new URL(moviesString);
                    // Set the value of the results String to the response from the HTTP request
                    results = NetworkUtils.getResponseFromHttpUrl(moviesURL);

                    // Put the JSON results string into our bundle
                    args.putString(JSON_MOVIES, results);
                } catch (IOException e){
                    e.printStackTrace();
                }
                return results;
            }

            @Override
            public void deliverResult(String data) {
                movieJSON = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data == null){
            displayError();
        } else {
            // Parse our JSONString
            try {
                // Make an object of our JSON String
                JSONObject object = new JSONObject(data);

                // Make an array of our JSON Object
                JSONArray array = object.getJSONArray("results");

                // Iterate over each JSONObject and add them to our ArrayList<Movie> variable
                for (int i = 0; i < array.length() ; i++){

                    // Create a movie object with the index of the array
                    Movie movie = new Movie(array.getJSONObject(i));
                    Log.d("Moviepath",movie.toString());

                    // Add the Movie Object to our ArrayList<Movie> movielist
                    movielist.add(movie);
                }
                // Show the JSON data
                showData();

                // Set the list of our adapter
                mAdapter.setList(movielist);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    // Helper method to setup the sharedPreferences
    private void setupSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadSortFromPreferences(preferences);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }


    // On changed prefrence we call the helper method
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.sort_key))) {
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
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
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
        if(id == R.id.sorting_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
