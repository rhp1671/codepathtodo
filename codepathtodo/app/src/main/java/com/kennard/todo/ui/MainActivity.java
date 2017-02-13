package com.kennard.todo.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kennard.todo.adapter.Task;
import com.kennard.todo.data.ToDoPersist;

import java.util.ArrayList;
import java.util.Date;


import com.kennard.todo.adapter.TaskAdapter;

import todo.kennard.com.codepathtodo.R;


public class MainActivity extends AppCompatActivity {
    ArrayList<Task> items;
    TaskAdapter itemsAdapter;
    ListView lvItems;
    public final static String EDIT_MSG = "com.kennard.edit.MESSAGE";
    public final static int EDIT_REQ = 2;
    public final static int ADD_REQ = 1;
    public final static int DEL_TASK = 3;
    int currentPosition;
    ToDoPersist storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        storage = new ToDoPersist(getApplicationContext());
        items = storage.readItems();
        lvItems = (ListView) findViewById(R.id.ivItems);
        itemsAdapter = new TaskAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_add:
                Intent intent = new Intent(this, EditActiviy.class);
                intent.putExtra("requestcode", ADD_REQ);
                startActivityForResult(intent, ADD_REQ);
                break;

            default:
                break;
        }

        return true;
    }


    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    storage.deleteItem(items.get(position));
                    items.remove(position);
                    itemsAdapter.notifyDataSetChanged();
                    currentPosition = position;
                } catch (Exception ex) {
                    showToast();
                }
                return true;
            }
        });


        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task val = items.get(position);
                currentPosition = position;
                Intent intent = new Intent(MainActivity.this, EditActiviy.class);
                intent.putExtra("requestcode", EDIT_REQ);
                intent.putExtra("taskclass", val);
                startActivityForResult(intent, EDIT_REQ);
            }
        });
    }

    public void onAddItem(Task task) {
        String val = task.mTask;
        if (!val.isEmpty()) {
            Date dt = task.mDate;
            String key = val + dt.getTime();
            task.mKey = key;
            try {
                storage.addItem(task);
                itemsAdapter.add(task);
            } catch (Exception ex) {
                showToast();
            }
        }
    }

    public void onDeleteItem(Task result) {
        try {
            items.set(currentPosition, result);
            Task task = items.get(currentPosition);
            storage.deleteItem(task);
            items.remove(currentPosition);
            itemsAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            showToast();
        }
    }


    public void onEditItem(Task result) {
        if (result != null) {
            try {
                Task task = items.get(currentPosition);
                if (result.mTask == null || result.mTask.isEmpty()) {
                    storage.deleteItem(task);
                    items.remove(currentPosition);
                } else {
                    storage.updateItem(result, task.mKey);
                    items.set(currentPosition, result);

                }
                itemsAdapter.notifyDataSetChanged();
            } catch (Exception ex) {
                showToast();
            }
        }
    }


    public void showToast() {
        Toast.makeText(this, getResources().getText(R.string.errorMsg),
                Toast.LENGTH_LONG).show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQ) {
            if (resultCode == RESULT_OK) {
                Task task = (Task) data.getParcelableExtra("taskclass");
                if (task != null) {
                    onAddItem(task);
                }
            }
        } else if (requestCode == EDIT_REQ) {
            if (resultCode == RESULT_OK) {
                Task task = (Task) data.getParcelableExtra("taskclass");
                if (task != null) {
                    onEditItem(task);
                }

            } else if (resultCode == DEL_TASK) {
                Task task = (Task) data.getParcelableExtra("taskclass");
                onDeleteItem(task);
            }
        }
    }
}
