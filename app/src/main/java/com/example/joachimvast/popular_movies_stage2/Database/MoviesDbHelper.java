package com.example.joachimvast.popular_movies_stage2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.joachimvast.popular_movies_stage2.Main.Movie;

/**
 * Created by JoachimVAST on 29/03/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    // Fields of our database
    // DBName
    private static final String DATABASE_NAME="movies.db";
    // DBVersion
    private static final int DATABASE_VERSION=2;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Query to create or table
        String SQL_CREATE_MOVIES_TABLE="CREATE TABLE IF NOT EXISTS " +
                MoviesDbContract.MovieEntry.TABLE_NAME + "(" +
                MoviesDbContract.MovieEntry.COLUMN_NAME_ID + " INTEGER NOT NULL, " +
                MoviesDbContract.MovieEntry.COLUMN_NAME_TITLE + " VARCHAR(45) NOT NULL, " +
                MoviesDbContract.MovieEntry.COLUMN_NAME_SORTING + " VARCHAR(20) NOT NULL, " +
                MoviesDbContract.MovieEntry.COLUMN_NAME_FAVOURITES + " INT, " +
                MoviesDbContract.MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT, " +
                MoviesDbContract.MovieEntry.COLUMN_NAME_RATING + " TEXT, " +
                MoviesDbContract.MovieEntry.COLUMN_NAME_RELEASE + " TEXT, " +
                MoviesDbContract.MovieEntry.COLUMN_NAME_THUMBNAIL + " TEXT NOT NULL" + ");";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
        Log.d("Table created", SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + MoviesDbContract.MovieEntry.TABLE_NAME);

        // Reinitialize
        onCreate(db);
    }

}
