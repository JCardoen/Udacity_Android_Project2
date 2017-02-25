package com.example.joachimvast.popular_movies_stage1;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.itemClickListener, AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<String>{


    // Declare variables
    TextView mError;
    ScrollView mScrollview;
    RecyclerView mRecyclerView;
    MovieAdapter mAdapter;
    Spinner mSorting;
    ArrayList<Movie> movielist = new ArrayList<Movie>();
    String sort;
    Boolean connection;
    ArrayAdapter<String> sAdapter;
    private static final String SEARCH_URL_API = "";
    private static final int LOADER_ID = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference ID to each variable
        mError = (TextView) findViewById(R.id.tv_error_msg);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_thumb);
        mSorting = (Spinner) findViewById(R.id.spinner_sorting);
        mScrollview = (ScrollView) findViewById(R.id.sv) ;

        // Make an array of String options for our Spinner
        String[] sorting = {"Sort by...","Popularity", "Top Rated"};

        // Initialize the adapter of our Spinner
        sAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,sorting);
        // Set the adapter of our Spinner
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSorting.setAdapter(sAdapter);
        mSorting.setOnItemSelectedListener(this);

        // Create a LayoutManager for the RecyclerView
        GridLayoutManager manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);

        // Set the LayoutManager for the RecyclerView object
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.hasFixedSize();

        // Make a new MovieAdapter object and set the adapter of the RecyclerView to that object
        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        // Start our Query on default it will display all 'popular' movies
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
            // Get our URL, pass on the sort variable
            URL apiUrl = NetworkUtils.buildUrl(this.sort);

            if(TextUtils.isEmpty(apiUrl.toString())){
                displayError();
            }

            Bundle queryBundle = new Bundle();
            queryBundle.putString(SEARCH_URL_API, apiUrl.toString());



        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieSearchLoader = loaderManager.getLoader(LOADER_ID);
        if (movieSearchLoader == null) {
            loaderManager.initLoader(LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(LOADER_ID, queryBundle, this);
        }
    }

    // Display an error
    public void displayError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mError.setVisibility(View.VISIBLE);
    }

    // Display our data
    public void showData() {
        mError.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(position) {

            // Set the String value based on what item of spinner was selected
            case 0: onNothingSelected(parent);
            case 1: this.sort = "popular"; makeQuery(); break;
            case 2: this.sort = "top_rated"; makeQuery(); break;
            default: break;
        }
        movielist.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        this.sort = "popular";
    }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String mResult;

            @Override
            protected void onStartLoading() {

                // If no arguments were passed, nothing to do. Just return.
                if(args == null){
                    return;
                }

                if(mResult != null){
                    deliverResult(mResult);
                }
                else {
                    forceLoad();
                }


            }

            @Override
            public String loadInBackground() {
                // Get the String using our SEARCH_URL_API key
                String searchUrlApi = args.getString(SEARCH_URL_API);

                // Initiate results String
                String results = null;

                // try - catch to catch any IOExceptions
                try{
                    // Form a URL object from our cached String
                    URL api = new URL(searchUrlApi);
                    // Set the value of the results String to the response from the HTTP request
                    results = NetworkUtils.getResponseFromHttpUrl(api);
                    return results;
                } catch (IOException e){
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                mResult = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (!connection ) {
            displayError();
        }
        else {

            // If the results from our HTTP request are not null, display the data
            if (data != null && !data.equals("")){

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
                        Log.d("MyActivity",movie.imagePath);

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
            else {
                // Display error
                displayError();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
