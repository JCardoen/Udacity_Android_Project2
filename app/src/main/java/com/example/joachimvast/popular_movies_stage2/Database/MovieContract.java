package com.example.joachimvast.popular_movies_stage2.Database;

import android.provider.BaseColumns;

/**
 * Created by JoachimVAST on 06/03/2017.
 */

public final class MovieContract {
    private MovieContract() {};

    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "Movies";
        public static final String COLUMN_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_THUMBNAIL = "thumbnail";
        public static final String COLUMN_FAVOURITES = "favourite";
        public static final String COLUMN_TOP_RATED = "top_rated";
        public static final String COLUMN_POPULAR = "popular";
    }

}
