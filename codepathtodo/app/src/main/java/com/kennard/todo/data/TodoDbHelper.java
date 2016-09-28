package com.kennard.todo.data;

/**
 * Created by raprasad on 9/26/16.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDbHelper extends SQLiteOpenHelper  {

    private static final String DB_NAME = "todo.db";
    private static final int DB_VERSION = 2;

   public TodoDbHelper(Context context){
       super(context, DB_NAME, null, DB_VERSION );
   }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TODO_TABLE = "CREATE TABLE " + TodoContract.TodoEntry.TABLE_NAME + " (" +
                TodoContract.TodoEntry._ID + " INTEGER PRIMARY KEY,"+
                TodoContract.TodoEntry.COLUMN_TASKS + " TEXT NOT NULL UNIQUE" +
                " )";

        db.execSQL(SQL_CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TodoContract.TodoEntry.TABLE_NAME);
        onCreate(db);
    }

}
