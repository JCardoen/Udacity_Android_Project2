package com.example.joachimvast.popular_movies_stage1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by JoachimVAST on 07/02/2017.
 */

public class Movie {

    // Creation of the fields
    public String title;
    public String release;
    public String overview;
    public JSONObject movie;
    public String imagePath;
    public String rating;
    public JSONObject trailersJSONObject;
    public JSONObject reviewsJSONObject;
    public String id;
    public String[] trailers;
    public String[] reviews;

    // Not-default constructor purely based on JSONObjects that we parse from the JSON String received from our HTTP Response
    public Movie (JSONObject movie){
        this.movie = movie;

        //  Get the name-value pairs from the JSONObject using the getString() method
        try {
            this.title = movie.getString("original_title");
            this.id = movie.getString("id");
            this.release = movie.getString("release_date");
            this.overview = movie.getString("overview");
            this.imagePath = "http://image.tmdb.org/t/p/w300" + movie.getString("poster_path");
            this.rating = movie.getString("vote_average");

            /*
            // For our trailer and reviews we need a new url built
            final String TRAILER_URL = "/videos";
            final String REVIEWS_URL = "/reviews";

            // Use method from NetworkUtils to create 2 new URLs
            URL trailerUrl = NetworkUtils.buildExtendedUrl(TRAILER_URL, this.id);
            URL reviewsUrl = NetworkUtils.buildExtendedUrl(REVIEWS_URL, this.id);

            // Get HTTP responses using the method from NetworkUtils class
            String trailersJSONString = NetworkUtils.getResponseFromHttpUrl(trailerUrl);
            String reviewsJSONString = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);

            // Parse the JSONString to JSONObject
            JSONObject trailer = new JSONObject(trailersJSONString);
            JSONObject reviews = new JSONObject(reviewsJSONString);

            JSONArray trailerResults = trailer.getJSONArray("results");
            JSONArray reviewsResults = trailer.getJSONArray("results");
            */


        } catch (JSONException e) {
            e.printStackTrace();
        } /* catch (IOException e) {
            e.printStackTrace();
        } */

    }

    public void setMovieTrailers (JSONObject trailers) {
        this.trailersJSONObject = trailers;
    }

    public void setMovieReviews (JSONObject reviews) {
        this.reviewsJSONObject = reviews;
    }

    // Create a toString method (debugging purposes)
    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", release='" + release + '\'' +
                ", overview='" + overview + '\'' +
                ", movie=" + movie +
                ", imagePath='" + imagePath + '\'' +
                ", rating=" + rating +
                '}';
    }
}
