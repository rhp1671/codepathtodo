package com.kennard.todo.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kennard.todo.adapter.Task;
import com.kennard.todo.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import todo.kennard.com.codepathtodo.R;

import static android.os.Build.VERSION_CODES.M;
import static com.kennard.todo.ui.MainActivity.EDIT_REQ;
import static todo.kennard.com.codepathtodo.R.id.chkCompleted;

public class EditActiviy extends AppCompatActivity {

    private Calendar myCalendar = Calendar.getInstance();
    private TextView taskView;
    private TextView dateView;
    private Task mTask;
    private RadioGroup radioPriority;
    private RadioButton radioUrgent;
    private RadioButton radioNormal;

    private int mode = -1;
    private CheckBox checkCompleted;
    private Menu mMenu;


    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

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

    private void updateLabel() {

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateView.setText(sdf.format(myCalendar.getTime()));
        mTask.mDate = myCalendar.getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        taskView = (TextView) findViewById(R.id.editItem);
        dateView = (TextView) findViewById(R.id.task_item_date);
        radioPriority = (RadioGroup) findViewById(R.id.radioPriority);
        radioUrgent = (RadioButton) findViewById(R.id.priorityUrgent);
        radioNormal = (RadioButton) findViewById(R.id.priorityNormal);
        checkCompleted = (CheckBox) findViewById(R.id.chkCompleted);

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDatePicker();
            }
        });

    }

    @Override
    public void onResume() {

        super.onResume();
        mode = getIntent().getExtras().getInt("requestcode");
        if (mode == MainActivity.ADD_REQ) {
            mTask = new Task();
            mTask.mDate = new Date();
            mTask.mPriority = 1;
            mTask.mCompleted = 0;

        } else {
            mTask = getIntent().getParcelableExtra("taskclass");
        }

        if (mTask.mPriority == 1) {
            radioPriority.check(R.id.priorityNormal);
        } else {
            radioPriority.check(R.id.priorityUrgent);
        }

        taskView.setText(mTask.mTask);

        dateView.setText(Utility.getFriendlyDayString(getApplicationContext(), mTask.mDate.getTime()));

        checkCompleted.setChecked(true);

        if (mTask.mCompleted == 1) {
            checkCompleted.setChecked(true);
        } else {
            checkCompleted.setChecked(false);
        }
        invalidateOptionsMenu();
    }


    private void launchDatePicker() {
        new DatePickerDialog(this, dateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        mMenu = menu;

        if (mode == MainActivity.ADD_REQ) {
            MenuItem item = mMenu.findItem(R.id.action_delete);
            item.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.home:
                finish();
                return true;

            case R.id.action_delete:
                Intent intentDel = new Intent();
                intentDel.putExtra("taskclass", mTask);
                setResult(MainActivity.DEL_TASK, intentDel);
                finish();
                break;

            case R.id.action_save:
                Intent intent = new Intent();
                String val = taskView.getText().toString();
                mTask.mTask = val;
                mTask.mPriority = radioPriority.getCheckedRadioButtonId() == R.id.priorityUrgent ? Task.URGENT : Task.NORMAL;

                if (mode == MainActivity.ADD_REQ) {
                    mTask.mKey = val + mTask.mDate.getTime();
                }
                mTask.mPriority = radioPriority.getCheckedRadioButtonId() == R.id.priorityUrgent ? Task.URGENT : Task.NORMAL;
                mTask.mCompleted = checkCompleted.isChecked() ? 1 : 0;
                intent.putExtra("taskclass", mTask);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}

