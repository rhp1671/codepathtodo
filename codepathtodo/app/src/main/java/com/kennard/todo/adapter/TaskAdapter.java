package com.kennard.todo.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kennard.todo.util.Utility;

import java.io.Console;
import java.util.ArrayList;

import todo.kennard.com.codepathtodo.R;

/**
 * Created by raprasad on 9/28/16.
 */
public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_detail, parent, false);
        }
        // Lookup view for data population
        TextView taskView = (TextView) convertView.findViewById(R.id.task_detail_textview);
        TextView taskDate = (TextView) convertView.findViewById(R.id.task_item_date);
        taskView.setText(task.mTask);
        taskDate.setText(Utility.getFriendlyDayString(getContext(), task.mDate.getTime()));
        if (task.mPriority == Task.NORMAL){
            taskView.setTextColor(ContextCompat.getColor(getContext(), R.color.teal));
        } else {
            taskView.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        }
        if (task.mCompleted == 1){
            taskView.setPaintFlags(taskView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            taskView.setPaintFlags( taskView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
        return convertView;
    }
}