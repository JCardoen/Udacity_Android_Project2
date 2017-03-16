package com.example.joachimvast.popular_movies_stage2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JoachimVAST on 06/03/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "movies.db";

    private static final int DB_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_MOVIES_TABLE =
                "CREATE TABLE" + MovieContract.MovieEntry.TABLE_NAME
                +" ("
                + MovieContract.MovieEntry.COLUMN_ID + "INTEGER PRIMARY KEY ,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + "VARCHAR(45) NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_THUMBNAIL + "TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_FAVOURITES + "BOOLEAN NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_TOP_RATED + "BOOLEAN NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_POPULAR + "BOOLEAN NOT NULL"
                + ");";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
