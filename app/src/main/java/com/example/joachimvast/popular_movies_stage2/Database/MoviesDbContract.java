package com.example.joachimvast.popular_movies_stage2.Database;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by JoachimVAST on 29/03/2017.
 */

public class MoviesDbContract {

    public static final String AUTHORITY = "com.example.joachimvast.popular_movies_stage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        // Declare variables for our columns and table name
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_SORTING = "sorting";
        public static final String COLUMN_NAME_THUMBNAIL = "thumbnail";
    }
}
