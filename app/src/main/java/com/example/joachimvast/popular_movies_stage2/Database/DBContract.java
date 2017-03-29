package com.example.joachimvast.popular_movies_stage2.Database;

import android.provider.BaseColumns;

/**
 * Created by JoachimVAST on 29/03/2017.
 */

public class DBContract implements BaseColumns{

    // Declare variables for our columns and table name
    public static final String TABLE_NAME="Movies";
    public static final String COLUMN_NAME_TITLE="title";
    public static final String COLUMN_NAME_ID="id";
    public static final String COLUMN_NAME_SORTING="sorting";
    public static final String COLUMN_NAME_THUMBNAILPATH="thumbnail_path";
}
