package com.kennard.todo.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.kennard.todo.adapter.Task;
import com.kennard.todo.data.ToDoPersist;
import com.kennard.todo.data.TodoContract;
import java.util.ArrayList;
import java.util.Date;

import com.kennard.todo.adapter.TaskAdapter;

import todo.kennard.com.codepathtodo.R;

public class MainActivity extends AppCompatActivity {
    ArrayList<Task> items;
    TaskAdapter itemsAdapter;
    ListView lvItems;
    public final static String EDIT_MSG = "com.kennard.edit.MESSAGE";
    public final static int EDIT_ITEM_REQUEST = 1;
    int currentPosition;
    ToDoPersist storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storage = new ToDoPersist(getApplicationContext());
        items = storage.readItems();
        lvItems = (ListView) findViewById(R.id.ivItems);
        itemsAdapter = new TaskAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                storage.deleteItem(items.get(currentPosition));
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });


        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task val = items.get(position);
                currentPosition = position;
                launchEditActivity(val.mTask);
            }
        });
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) (findViewById (R.id.etNewItem));
        String val = etNewItem.getText().toString();
        Task task = new Task(val, new Date(), Task.NORMAL);
        itemsAdapter.add(task);
        etNewItem.setText("");
        storage.writeItems(task);
    }

    private void launchEditActivity(String text){
        Intent intent = new Intent(this, EditItemActivity.class);
        Task task = items.get(currentPosition);
        intent.putExtra(EDIT_MSG, task);
        startActivityForResult(intent,EDIT_ITEM_REQUEST );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST){
            Task result = data.getParcelableExtra(EDIT_MSG);
            if (result.mTask == null || result.mTask.isEmpty()){
                Task task = items.get(currentPosition);
                storage.deleteItem(task);
                items.remove(currentPosition);
            } else {
                items.set(currentPosition, result);
                storage.updateItem(result);
            }
            itemsAdapter.notifyDataSetChanged();
        }
    }

}
