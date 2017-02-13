package com.kennard.todo.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by raprasad on 9/28/16.
 */
public class Task implements Parcelable {
    public String mTask;
    public int mPriority;
    public Date mDate;
    public String mKey;
    public int mCompleted;
    public static int URGENT = 2;
    public static int NORMAL = 1;

    public  Task(String task, Date date, int priority, String key){
        mTask = task;
        mDate = date;
        mPriority = priority;
        mKey = key;
    }

    public Task(Parcel source){
        mTask=source.readString();
        mDate=new Date(source.readLong());
        mPriority=source.readInt();
        mKey=source.readString();
        mCompleted = source.readInt();
    }

    public Task(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTask);
        dest.writeLong(mDate.getTime());
        dest.writeInt(mPriority);
        dest.writeString(mKey);
        dest.writeInt(mCompleted);
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }

        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }
    };
}
