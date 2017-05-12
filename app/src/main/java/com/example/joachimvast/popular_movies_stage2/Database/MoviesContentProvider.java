package com.example.joachimvast.popular_movies_stage2.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Joachim on 04/05/2017.
 */

public class MoviesContentProvider extends ContentProvider {

    // Member variable for our MoviesDbHelper

    private MoviesDbHelper helper;
    @Override
    public boolean onCreate() {

        // Initialize our MoviesDbHelper variable
        helper = new MoviesDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = helper.getReadableDatabase();

        int match = uriMatcher.match(uri);
        String[] columns = {MoviesDbContract.MovieEntry.COLUMN_NAME_THUMBNAIL};
        Cursor cursor;

        switch(match) {
            case MOVIES:

                cursor = db.query(MoviesDbContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                Log.d("Cursor:", DatabaseUtils.dumpCursorToString(cursor));
                break;
            default:
                throw new UnsupportedOperationException("Unknow uri :" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = helper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        Uri returnUri;

        switch(match) {
            case MOVIES:
                long id = db.insert(MoviesDbContract.MovieEntry.TABLE_NAME, null, values);

                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(MoviesDbContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public static final int MOVIES = 100;
    public static final int MOVIES_BY_ID = 101;


    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesDbContract.AUTHORITY, MoviesDbContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesDbContract.AUTHORITY, MoviesDbContract.PATH_MOVIES + "/#", MOVIES_BY_ID);

        return uriMatcher;
    }
}