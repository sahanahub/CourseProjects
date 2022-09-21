package com.example.homework03;

//Homework03
//ToDoListFragment.java
//Sahana Srinivas

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.homework03.databinding.FragmentToDoListBinding;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ToDoListFragment extends Fragment {

    FragmentToDoListBinding todolistBinding;
    private static final String ARG_PARAM_TASK_LIST = "ARG_PARAM_TASK_LIST";
    private ArrayList<Task> mTasks;
    final String space=" ";
    FragmentInterface mListener;
    String showDate, finalDateNew;

    public ToDoListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ToDoListFragment newInstance(ArrayList<Task> task_list) {
        ToDoListFragment fragment = new ToDoListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM_TASK_LIST, task_list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTasks = getArguments().getParcelableArrayList(ARG_PARAM_TASK_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        todolistBinding = FragmentToDoListBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_todolist_activity));
        return todolistBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        todolistBinding.btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.createTask();
            }
        });

        todolistBinding.btnViewtask.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View view) {
                                                               try{
                                                                   if (mTasks.size() == 0) {
                                                                       throw new Exception();
                                                                   } else {
                                                                       String[] charSequence = new String[mTasks.size()];
                                                                       for (int i = 0; i < mTasks.size(); i++) {
                                                                           charSequence[i] = String.valueOf(mTasks.get(i).taskName);
                                                                       }

                                                                       AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                                       builder.setTitle(R.string.label_alert_title_list)
                                                                               .setItems(charSequence, new DialogInterface.OnClickListener() {
                                                                                   public void onClick(DialogInterface dialog, int which) {

                                                                                       mListener.viewTask(new Task(mTasks.get(which).taskName, mTasks.get(which).taskDate, mTasks.get(which).taskPriority),which);

//
                                                                                   }
                                                                               }).setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
                                                                           @Override
                                                                           public void onClick(DialogInterface dialogInterface, int i) {
                                                                               dialogInterface.dismiss();
                                                                           }
                                                                       });
                                                                       builder.show();
                                                                   }

                                                               }catch(Exception e){
                                                                   Toast.makeText(getContext(), getString(R.string.label_empty_list), Toast.LENGTH_SHORT).show();
                                                               }

                                                           }
                                                       }
        );




    }

    @Override
    public void onResume() {
        super.onResume();

        if (mTasks.isEmpty()) {
            String youHave= getString(R.string.label_ToDoYouHave) +space+getString(R.string.label_zero)+space+getString(R.string.label_ToDoYouHave_task);
            todolistBinding.tvToDoYouHave.setText(youHave);
            todolistBinding.tvToDoName.setText(R.string.label_ToDoNone);
            todolistBinding.tvToDoPriority.setText("");

        }else {
            Collections.sort(mTasks, new SortTaskDate());
            String youHave= getString(R.string.label_ToDoYouHave) +space+mTasks.size()+space+getString(R.string.label_ToDoYouHave_task);
            todolistBinding.tvToDoYouHave.setText(youHave);

            todolistBinding.tvToDoName.setText(mTasks.get(0).taskName);
            showDate = mTasks.get(0).taskDate;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date objDate = dateFormat.parse(showDate);
                //Expected date format
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy");
                finalDateNew = dateFormat2.format(objDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            todolistBinding.tvToDoDate.setText(finalDateNew);
            todolistBinding.tvToDoPriority.setText(mTasks.get(0).taskPriority);
        }

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