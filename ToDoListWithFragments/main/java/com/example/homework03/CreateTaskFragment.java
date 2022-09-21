package com.example.homework03;

//Homework03
//CreateTaskFragment.java
//Sahana Srinivas

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.homework03.databinding.FragmentCreateTaskBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateTaskFragment extends Fragment {

    FragmentCreateTaskBinding createTaskBinding;
    FragmentInterface mListener;
    String finalDate;
    String finalDateNew;

    public CreateTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createTaskBinding = FragmentCreateTaskBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_CreateTask));
        return createTaskBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createTaskBinding.radioHigh.setChecked(true);

        createTaskBinding.btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar newCalendar = Calendar.getInstance();

                final DatePickerDialog StartTime = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth);

                                createTaskBinding.tvDateValue.setText((dayOfMonth + " / " + (monthOfYear + 1) + " / " + year));
                                finalDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }

                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                StartTime.getDatePicker().setMinDate(newCalendar.getTimeInMillis());
                StartTime.show();
            }
        });

        createTaskBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String priorityType = getString(R.string.label_high);
                int priorityValue = createTaskBinding.radioPriority.getCheckedRadioButtonId();
                if (priorityValue == R.id.radioHigh) {
                    priorityType = getResources().getString(R.string.label_high);
                } else if (priorityValue == R.id.radioMedium) {
                    priorityType = getResources().getString(R.string.label_medium);
                } else if (priorityValue == R.id.radioLow) {
                    priorityType = getResources().getString(R.string.label_low);
                }

                boolean validationResult = validation();
                if (validationResult) {

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date objDate = dateFormat.parse(finalDate);
                        //Expected date format
                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                        finalDateNew = dateFormat2.format(objDate);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                mListener.addTask(new Task(createTaskBinding.etTaskName.getText().toString(),finalDateNew,priorityType));

                }
            }
        });

        createTaskBinding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancelCreateTask();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof FragmentInterface) {
                mListener = (FragmentInterface) context;
            } else {
                throw new RuntimeException();
            }

        } catch (RuntimeException e) {
        }
    }

    public boolean validation() {

        if (createTaskBinding.etTaskName.getText().toString().length() == 0) {
            Toast.makeText(getContext(), getString(R.string.label_validation_error_taskname), Toast.LENGTH_SHORT).show();
            return false;
        } else if (createTaskBinding.tvDateValue.length() == 0) {
            Toast.makeText(getContext(), getString(R.string.label_validation_error_setDate), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}