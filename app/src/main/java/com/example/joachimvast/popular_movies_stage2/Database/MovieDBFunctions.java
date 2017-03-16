package com.example.joachimvast.popular_movies_stage2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.joachimvast.popular_movies_stage2.Movie;

/**
 * Created by JoachimVAST on 15/03/2017.
 */

public interface MovieDBFunctions {
    public void insertMovie(SQLiteDatabase db, boolean popular, boolean top_rated, boolean  favourite, Movie m);
    public Cursor getMoviesPopular(SQLiteDatabase db);
    public Cursor getMoviesTop(SQLiteDatabase db);
    public Cursor getMoviesfavourites(SQLiteDatabase db);
}
