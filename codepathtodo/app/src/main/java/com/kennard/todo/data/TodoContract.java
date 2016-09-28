package com.kennard.todo.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by raprasad on 9/26/16.
 */
public class TodoContract {

    public static final String CONTENT_AUTHORITY = "com.kennard.todo.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TODO = "todo";

    public static final class TodoEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_TODO).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TODO;

        public static final String TABLE_NAME = "todo";

        public static final String COLUMN_TASKS = "tasks";

        public static Uri buildTodoUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }



}
