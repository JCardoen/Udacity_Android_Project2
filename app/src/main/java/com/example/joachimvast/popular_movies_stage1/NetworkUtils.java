package com.example.joachimvast.popular_movies_stage1;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by JoachimVAST on 06/02/2017.
 */

public class NetworkUtils {

    // This is our preconfigured base URL
    final static String BASE_URL = "http://api.themoviedb.org/3/movie/";
    final static String QUERY_PARAM = "?";


    final static String API = "api_key=";

    // To insert your own API_KEY, insert it into the KEY variable
    final static String KEY = "";

    public static URL buildUrl(String sort) {
    String URL = BASE_URL + sort + QUERY_PARAM + API + KEY;
        // Built our URI
        Uri builtUri = Uri.parse(URL).buildUpon()
                .build();
        URL url = null;

        // Try to form a URL using the toString() method from the URL class
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
