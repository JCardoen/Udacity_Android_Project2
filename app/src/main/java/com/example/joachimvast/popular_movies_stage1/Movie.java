package com.example.joachimvast.popular_movies_stage1;

import org.json.JSONException;
import org.json.JSONObject;

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

    // Not-default constructor purely based on JSONObjects that we parse from the JSON String received from our HTTP Response
    public Movie (JSONObject movie){
        this.movie = movie;

        //  Get the name-value pairs from the JSONObject using the getString() method
        try {
            this.title = movie.getString("original_title");
            this.release = movie.getString("release_date");
            this.overview = movie.getString("overview");
            this.imagePath = "http://image.tmdb.org/t/p/w300" + movie.getString("poster_path");
            this.rating = movie.getString("vote_average");
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
