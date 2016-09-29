package com.kennard.todo.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kennard.todo.adapter.Task;
import com.kennard.todo.util.Utility;

import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;

import todo.kennard.com.codepathtodo.R;

public class EditItemActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    TextView dateView;
    Task mTask;
    RadioGroup radioPriority;
    RadioButton radioUrgent;
    RadioButton radioNormal;

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        TextView textView = (TextView) findViewById(R.id.editItem);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
           mTask = bundle.getParcelable(MainActivity.EDIT_MSG);
            if (mTask != null){
                textView.setText(mTask.mTask);
            }
        }
        dateView = (TextView) findViewById(R.id.task_item_date);
        dateView.setText( Utility.getFriendlyDayString(getApplicationContext(), mTask.mDate.getTime()));
        myCalendar.setTime(mTask.mDate);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDatePicker();
            }
        });
        radioPriority = (RadioGroup) findViewById(R.id.radioPriority);
        radioUrgent = (RadioButton) findViewById(R.id.priorityUrgent);
        radioNormal = (RadioButton) findViewById(R.id.priorityNormal);
        if (mTask.mPriority == 1){
            radioPriority.check(R.id.priorityNormal);
        } else{
            radioPriority.check(R.id.priorityUrgent);
        }
    }


    private void launchDatePicker(){
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    protected void onSave(View v){
        TextView textView = (TextView) findViewById(R.id.editItem);
        String val = textView.getText().toString();
        Intent intent = new Intent();
        Task task = new Task(val, myCalendar.getTime(), radioPriority.getCheckedRadioButtonId()==R.id.priorityUrgent? Task.URGENT: Task.NORMAL);
        intent.putExtra(MainActivity.EDIT_MSG, task );
        setResult(MainActivity.EDIT_ITEM_REQUEST, intent);
        finish();
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateView.setText(sdf.format(myCalendar.getTime()));
    }

}
