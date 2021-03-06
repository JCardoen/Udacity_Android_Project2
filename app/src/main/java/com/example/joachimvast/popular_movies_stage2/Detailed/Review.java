package com.example.joachimvast.popular_movies_stage2.Detailed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joachim on 14/05/2017.
 */

public class Review {
    String author;
    String content;

    // Non-default constructor that parses the JSONObject
    public Review(JSONObject obj) {
        try {
            this.author = obj.getString("author");
            this.content = obj.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return this.author + ": \n" + this.content;
    }

    public String getAuthor() {
        return "Author" + this.author;
    }

    public String getContent() {
        return this.content;
    }
}
