package com.kennard.todo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by raprasad on 9/27/16.
 */
public class ToDoPersist {

    private Context mContext;
    static final int COL_TASK = 1;

    public ToDoPersist (Context context){
        mContext = context;
    }

    public ArrayList readItems(){
        ArrayList<String> items  = new ArrayList<>();
        Cursor c = mContext.getContentResolver().query(TodoContract.TodoEntry.CONTENT_URI,
                null,
                null,
                null,
                null,
                null
        );
        while(c.moveToNext()) {
            items.add(c.getString(COL_TASK));
        }
        c.close();
        return items;
    }

    public void writeItems(String task){
        ContentValues value = new ContentValues();
        value.put(TodoContract.TodoEntry.COLUMN_TASKS, task);
        mContext.getContentResolver().insert(TodoContract.TodoEntry.CONTENT_URI, value);
    }
    public void updateItem(String task) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(TodoContract.TodoEntry.COLUMN_TASKS, task);
        mContext.getContentResolver().update(TodoContract.TodoEntry.CONTENT_URI,updatedValues, TodoContract.TodoEntry._ID+"=?", new String[]{String.valueOf(COL_TASK)});
    }

    public void deleteItem(String task) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(TodoContract.TodoEntry.COLUMN_TASKS, task);
        mContext.getContentResolver().delete(TodoContract.TodoEntry.CONTENT_URI, TodoContract.TodoEntry.COLUMN_TASKS + "=?" , new String[]{task});
    }
}
