package com.kennard.todo.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by raprasad on 9/26/16.
 */
public class TodoProvider extends ContentProvider {
    private TodoDbHelper mHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int TODO = 100;

    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher  = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TodoContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, TodoContract.PATH_TODO, TODO);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mHelper = new TodoDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)){
            case TODO:
                retCursor = mHelper.getReadableDatabase().query(TodoContract.TodoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case TODO:
                return TodoContract.TodoEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TODO:
                long id = db.insert(TodoContract.TodoEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = TodoContract.TodoEntry.buildTodoUri(id);
                } else{
                    throw new UnsupportedOperationException("Unknown Uri" + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowDeleted;
        switch (match) {
            case TODO:
                rowDeleted = db.delete(TodoContract.TodoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
       if (rowDeleted != 0){
           getContext().getContentResolver().notifyChange(uri, null);
       }
        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case TODO:
                rowsUpdated = db.update(TodoContract.TodoEntry.TABLE_NAME,values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
