package com.kennard.todo.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kennard.todo.adapter.Task;
import com.kennard.todo.data.ToDoPersist;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import com.kennard.todo.adapter.TaskAdapter;

import todo.kennard.com.codepathtodo.R;

public class MainActivity extends AppCompatActivity implements EditItemDialogFragment.EditItemDialogListener {
    ArrayList<Task> items;
    TaskAdapter itemsAdapter;
    ListView lvItems;
    public final static String EDIT_MSG = "com.kennard.edit.MESSAGE";
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
                showItemDetailDialog();
            }
        });
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) (findViewById(R.id.etNewItem));
        String val = etNewItem.getText().toString();
        if (!val.isEmpty()) {
            Date dt = Calendar.getInstance().getTime();
            Task task = new Task(val,dt, Task.NORMAL, val+dt.getTime());
            etNewItem.setText("");
            try {
                storage.addItem(task);
                itemsAdapter.add(task);
            } catch (Exception ex) {
                showToast();
            }
        }
    }


    @Override
    public void onFinishEditDialog(Task result) {
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

    private void showItemDetailDialog() {
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EDIT_MSG, items.get(currentPosition));
        EditItemDialogFragment editDialog = EditItemDialogFragment.newInstance(bundle);
        editDialog.show(fm, "fragment_alert");
    }

    public void showToast() {
        Toast.makeText(this, getResources().getText(R.string.errorMsg),
                Toast.LENGTH_LONG).show();
    }
}
