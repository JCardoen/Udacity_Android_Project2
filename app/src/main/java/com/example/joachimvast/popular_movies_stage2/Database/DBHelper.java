package com.example.joachimvast.popular_movies_stage2.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        String SQL_CREATE_MOVIES_TABLE="CREATE TABLE" +
                DBContract.TABLE_NAME + "(" +
                DBContract.COLUMN_NAME_ID + "INTEGER PRIMARY KEY," +
                DBContract.COLUMN_NAME_TITLE + "VARCHAR(45) NOT NULL, " +
                DBContract.COLUMN_NAME_SORTING + "VARCHAR(20) NOT NULL," +
                DBContract.COLUMN_NAME_THUMBNAILPATH + "TEXT NOT NULL" + ");";

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
        String[] columns = {DBContract.COLUMN_NAME_THUMBNAILPATH};
        return db.query(DBContract.TABLE_NAME,
                columns,
                "WHERE" + DBContract.COLUMN_NAME_SORTING + "= " + sort,
                null,
                null,
                null,
                null
        );
    }
}
