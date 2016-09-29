package com.kennard.todo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.kennard.todo.adapter.Task;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by raprasad on 9/27/16.
 */
public class ToDoPersist {

    private Context mContext;
    static final int COL_TASK = 1;
    static final int COL_DATE = 2;
    static final int COL_PRIORITY = 3;

    private static final String[] TODO_COLUMNS = {
            TodoContract.TodoEntry.TABLE_NAME + "." + TodoContract.TodoEntry._ID,
            TodoContract.TodoEntry.COLUMN_TASKS,
            TodoContract.TodoEntry.COLUMN_DATE,
            TodoContract.TodoEntry.COLUMN_PRIORITY
    };

    public ToDoPersist (Context context){
        mContext = context;
    }

    public ArrayList readItems(){
        ArrayList<Task> items  = new ArrayList<>();
        Cursor c = mContext.getContentResolver().query(TodoContract.TodoEntry.CONTENT_URI,
                null,
                null,
                null,
                null,
                null
        );
        while(c.moveToNext()) {
            Task task = new Task(c.getString(COL_TASK), new Date(c.getInt(COL_DATE)), c.getInt(COL_PRIORITY));
            items.add(task);
        }
        c.close();
        return items;
    }

    public void writeItems(Task task){
        ContentValues value = new ContentValues();
        value.put(TodoContract.TodoEntry.COLUMN_TASKS, task.mTask);
        value.put(TodoContract.TodoEntry.COLUMN_DATE, task.mDate.getTime());
        value.put(TodoContract.TodoEntry.COLUMN_PRIORITY, task.mPriority);
        mContext.getContentResolver().insert(TodoContract.TodoEntry.CONTENT_URI, value);
    }
    public void updateItem(Task task) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(TodoContract.TodoEntry.COLUMN_TASKS, task.mTask);
        updatedValues.put(TodoContract.TodoEntry.COLUMN_DATE, task.mDate.getTime());
        updatedValues.put(TodoContract.TodoEntry.COLUMN_PRIORITY, task.mPriority);
        mContext.getContentResolver().update(TodoContract.TodoEntry.CONTENT_URI,updatedValues, TodoContract.TodoEntry.COLUMN_TASKS+"=?", new String[]{String.valueOf(COL_TASK)});
    }

    public void deleteItem(Task task) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(TodoContract.TodoEntry.COLUMN_TASKS, task.mTask);
        mContext.getContentResolver().delete(TodoContract.TodoEntry.CONTENT_URI, TodoContract.TodoEntry.COLUMN_TASKS + "=?" , new String[]{task.mTask});
    }
}
