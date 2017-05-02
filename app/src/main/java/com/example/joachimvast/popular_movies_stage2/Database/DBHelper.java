package com.example.joachimvast.popular_movies_stage2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.example.joachimvast.popular_movies_stage2.Main.Movie;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by JoachimVAST on 29/03/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    // Fields of our database
    // DBName
    private static final String DATABASE_NAME="movies.db";
    // DBVersion
    private static final int DATABASE_VERSION=1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Query to create or table
        String SQL_CREATE_MOVIES_TABLE="CREATE TABLE IF NOT EXISTS" +
                DBContract.TABLE_NAME + "(" +
                DBContract.COLUMN_NAME_ID + " INTEGER PRIMARY KEY NOT NULL," +
                DBContract.COLUMN_NAME_TITLE + " VARCHAR(45) NOT NULL, " +
                DBContract.COLUMN_NAME_SORTING + " VARCHAR(20) NOT NULL," +
                DBContract.COLUMN_NAME_THUMBNAIL + " TEXT NOT NULL" + ");";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop table
        db.execSQL("DROP TABLE IF EXISTS" + DBContract.TABLE_NAME);

        // Reinitialize
        onCreate(db);
    }

    public Cursor getThumbnails(SQLiteDatabase db, String sort) {
        // Get the thumbnails and store it as a cursor
        String[] columns = {DBContract.COLUMN_NAME_THUMBNAIL};
        return db.query(DBContract.TABLE_NAME,
                columns,
                DBContract.COLUMN_NAME_SORTING + " = " + "" + sort + "",
                null,
                null,
                null,
                null
        );
    }

    public void insertMovie(Movie movie, SQLiteDatabase db, String sort) {

        // ContentValues instance to pass values into our insert query
        ContentValues cv = new ContentValues();

        // Put key-value pairs into our ContentValues object
        cv.put(DBContract.COLUMN_NAME_ID, movie.id);
        cv.put(DBContract.COLUMN_NAME_SORTING, sort);
        cv.put(DBContract.COLUMN_NAME_THUMBNAIL, movie.imagePath);
        cv.put(DBContract.COLUMN_NAME_TITLE, movie.title);

        // Insert query
        db.update(DBContract.TABLE_NAME, cv,  "id=" + movie.id, null);
    }

    public void markFavourite(String id, SQLiteDatabase db) {

        // ContentValues instance to pass values into our insert query
        ContentValues cv = new ContentValues();

        // Put key-value pair into our ContentValues object, we want to have sorting as favourite
        cv.put(DBContract.COLUMN_NAME_SORTING, "favourites");

        // Update query
        db.update(DBContract.TABLE_NAME, cv, "id="+id, null);
    }

    public void unmarkFavourite(String id, SQLiteDatabase db) {

        // ContentValues instance to pass values into our insert query
        ContentValues cv = new ContentValues();

        // Put key-value pair into our ContentValues object, we want to have sorting as favourite
        cv.put(DBContract.COLUMN_NAME_SORTING, "");

        // Update query
        db.update(DBContract.TABLE_NAME, cv, "id="+id, null);
    }
}
