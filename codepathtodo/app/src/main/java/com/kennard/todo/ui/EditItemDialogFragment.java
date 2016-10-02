package com.kennard.todo.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kennard.todo.adapter.Task;
import com.kennard.todo.util.Utility;

import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;

import todo.kennard.com.codepathtodo.R;

public class EditItemDialogFragment extends DialogFragment {

    Calendar myCalendar = Calendar.getInstance();
    TextView taskView;
    TextView dateView;
    Task mTask;
    RadioGroup radioPriority;
    RadioButton radioUrgent;
    RadioButton radioNormal;

    public interface EditItemDialogListener {
        void onFinishEditDialog(Task task);
    }

    private EditItemDialogListener listener;

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

    public EditItemDialogFragment() {

    }

    public static EditItemDialogFragment newInstance(Bundle args) {
        EditItemDialogFragment frag = new EditItemDialogFragment();
        frag.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Task task  = getArguments().getParcelable(MainActivity.EDIT_MSG);
        mTask = new Task(task.mTask, task.mDate, task.mPriority, task.mKey);
        taskView = (TextView) view.findViewById(R.id.editItem);
        if (mTask != null) {
            taskView.setText(mTask.mTask);
        }
        getDialog().setTitle(getResources().getString(R.string.editTitle));
        TextView title = (TextView)getDialog().findViewById( android.R.id.title );
        title.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        dateView = (TextView) view.findViewById(R.id.task_item_date);
        dateView.setText(Utility.getFriendlyDayString(getContext(), mTask.mDate.getTime()));
        myCalendar.setTime(mTask.mDate);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDatePicker();
            }
        });
        radioPriority = (RadioGroup) view.findViewById(R.id.radioPriority);
        radioUrgent = (RadioButton) view.findViewById(R.id.priorityUrgent);
        radioNormal = (RadioButton) view.findViewById(R.id.priorityNormal);
        if (mTask.mPriority == 1) {
            radioPriority.check(R.id.priorityNormal);
        } else {
            radioPriority.check(R.id.priorityUrgent);
        }

        Button save = (Button) view.findViewById(R.id.button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });

        Button cancel = (Button) view.findViewById(R.id.buttonCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (EditItemDialogListener) context;
        } catch (Exception ex) {

        }
    }

    private void launchDatePicker() {
        new DatePickerDialog(getContext(), dateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void Save() {
        String val = taskView.getText().toString();
        mTask.mTask=val;
        mTask.mPriority = radioPriority.getCheckedRadioButtonId() == R.id.priorityUrgent ? Task.URGENT : Task.NORMAL;
        mTask.mKey=val+mTask.mDate.getTime();
        listener.onFinishEditDialog(mTask);
        dismiss();
    }


    private void updateLabel() {

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateView.setText(sdf.format(myCalendar.getTime()));
        mTask.mDate = myCalendar.getTime();
    }

    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;

        window.setLayout((int) (width * 0.80), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

}
