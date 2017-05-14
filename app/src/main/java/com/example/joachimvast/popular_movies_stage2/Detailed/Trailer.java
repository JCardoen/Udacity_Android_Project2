package com.example.joachimvast.popular_movies_stage2.Detailed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joachim on 14/05/2017.
 */

public class Trailer {
    private String name;
    private String key;

    // Base url to view youtube videos
    private final String BASE_URL = "https://www.youtube.com/watch?v=";

    // Non-default constructor that parses the JSONObject
    public Trailer(JSONObject obj) {
        try {
            this.name = obj.getString("name");
            this.key = obj.getString("key");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTrailerURL() {
        return BASE_URL + this.key;
    }

    public String toString() {
        return this.name;
    }
}
