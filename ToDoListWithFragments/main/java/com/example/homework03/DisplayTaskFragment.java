package com.example.homework03;

//Homework03
//DisplayTaskFragment.java
//Sahana Srinivas

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.homework03.databinding.FragmentDisplayTaskBinding;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DisplayTaskFragment extends Fragment {

    private static final String ARG_PARAM_TASK = "ARG_PARAM_TASK";
    private static final String ARG_PARAM_DELETE_INDEX = "ARG_PARAM_DELETE_INDEX";
    FragmentDisplayTaskBinding displayTaskBinding;
    FragmentInterface mListener;
    private Task taskobj;
    String finalDateNew;
    int KEY_Delete;


    public DisplayTaskFragment() {
        // Required empty public constructor
    }


    public static DisplayTaskFragment newInstance(Task task, int deleteIndex) {
        DisplayTaskFragment fragment = new DisplayTaskFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_TASK, task);
        args.putInt(ARG_PARAM_DELETE_INDEX, deleteIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskobj = getArguments().getParcelable(ARG_PARAM_TASK);
            KEY_Delete = getArguments().getInt(ARG_PARAM_DELETE_INDEX,-1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        displayTaskBinding = FragmentDisplayTaskBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_todo_display_activity));
        return displayTaskBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (taskobj != null)
        {
            displayTaskBinding.tvTaskNameRes.setText(taskobj.taskName);

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date objDate = dateFormat.parse(taskobj.taskDate);
                //Expected date format
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy");
                finalDateNew = dateFormat2.format(objDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            displayTaskBinding.tvTaskDateRes.setText(finalDateNew);

            displayTaskBinding.tvTaskPriorityRes.setText(taskobj.taskPriority);

        }
        displayTaskBinding.btnDisplayCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancelCreateTask();
            }
        });

        displayTaskBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.deleteTask(KEY_Delete);
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
}