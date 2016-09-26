package todo.kennard.com.codepathtodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        TextView textView = (TextView) findViewById(R.id.editItem);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String val = bundle.getString(MainActivity.EDIT_MSG);
            if (val != null){
                textView.setText(val);
            }
        }
    }


    protected void onSave(View v){
        TextView textView = (TextView) findViewById(R.id.editItem);
        String val = textView.getText().toString();
        Intent intent = new Intent();
        intent.putExtra(MainActivity.EDIT_MSG, val );
        setResult(MainActivity.EDIT_ITEM_REQUEST, intent);
        finish();
    }
}
