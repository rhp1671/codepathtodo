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

import com.kennard.todo.data.ToDoPersist;
import com.kennard.todo.data.TodoContract;
import java.util.ArrayList;

import todo.kennard.com.codepathtodo.R;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
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
        itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items );
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
                String val = items.get(position);
                currentPosition = position;
                launchEditActivity(val);
            }
        });

    }



    public void onAddItem(View v){
        EditText etNewItem = (EditText) (findViewById (R.id.etNewItem));
        String val = etNewItem.getText().toString();
        itemsAdapter.add(val);
        etNewItem.setText("");
        storage.writeItems(val);
    }

    private void launchEditActivity(String text){
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra(EDIT_MSG, text);
        startActivityForResult(intent,EDIT_ITEM_REQUEST );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST){
           String val =  data.getStringExtra(EDIT_MSG);
            if (val == null || val.isEmpty()){
                storage.deleteItem(items.get(currentPosition));
                items.remove(currentPosition);
            } else {
                items.set(currentPosition, val);
                storage.updateItem(val);
            }
            itemsAdapter.notifyDataSetChanged();
        }
    }
}
