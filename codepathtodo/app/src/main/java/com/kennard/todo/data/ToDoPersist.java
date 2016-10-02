package com.kennard.todo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.kennard.todo.adapter.Task;
import com.kennard.todo.util.Utility;

import java.util.ArrayList;
import java.util.Date;

import todo.kennard.com.codepathtodo.R;

/**
 * Created by raprasad on 9/27/16.
 */
public class ToDoPersist {

    private Context mContext;
    static final int COL_TASK = 1;
    static final int COL_DATE = 2;
    static final int COL_PRIORITY = 3;
    static final int COL_KEY = 4;

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
            Task task = new Task(c.getString(COL_TASK), Utility.formatDateTime(c.getString(COL_DATE)), c.getInt(COL_PRIORITY), c.getString(COL_KEY));
            items.add(task);
        }
        c.close();
        return items;
    }

    public void addItem(Task task){
            ContentValues value = new ContentValues();
            value.put(TodoContract.TodoEntry.COLUMN_TASKS, task.mTask);
            value.put(TodoContract.TodoEntry.COLUMN_DATE, Utility.formatDateTime(task.mDate));
            value.put(TodoContract.TodoEntry.COLUMN_PRIORITY, task.mPriority);
            value.put(TodoContract.TodoEntry.COLUMN_KEY, task.mKey);
            mContext.getContentResolver().insert(TodoContract.TodoEntry.CONTENT_URI, value);
    }
    public void updateItem(Task task, String key) {
            ContentValues updatedValues = new ContentValues();
            updatedValues.put(TodoContract.TodoEntry.COLUMN_TASKS, task.mTask);
            updatedValues.put(TodoContract.TodoEntry.COLUMN_DATE, Utility.formatDateTime(task.mDate));
            updatedValues.put(TodoContract.TodoEntry.COLUMN_PRIORITY, task.mPriority);
            updatedValues.put(TodoContract.TodoEntry.COLUMN_KEY, task.mKey);
            mContext.getContentResolver().update(TodoContract.TodoEntry.CONTENT_URI, updatedValues, TodoContract.TodoEntry.COLUMN_KEY + "=?", new String[]{key});
    }

    public void deleteItem(Task task) {
            mContext.getContentResolver().delete(TodoContract.TodoEntry.CONTENT_URI, TodoContract.TodoEntry.COLUMN_KEY + "=?",
                    new String[]{task.mKey});
    }
}
