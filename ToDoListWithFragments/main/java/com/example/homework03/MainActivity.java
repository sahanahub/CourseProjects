package com.example.homework03;

//Homework03
//MainActivity.java
//Sahana Srinivas

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentInterface {

    ArrayList<Task> tasks = new ArrayList<>();
    Task mtask;
    int deleteIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerMain,ToDoListFragment.newInstance(tasks),getString(R.string.label_todolist_activity))
                .commit();
    }

    @Override
    public void createTask() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain,new CreateTaskFragment(), getString(R.string.label_CreateTask))
                .addToBackStack(getString(R.string.label_todolist_activity))
                .commit();
    }

    @Override
    public void viewTask(Task task, int deleteIndex) {
        mtask = task;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain,DisplayTaskFragment.newInstance(mtask, deleteIndex), getString(R.string.label_todo_display_activity))
                .addToBackStack(getString(R.string.label_todolist_activity))
                .commit();
    }

    @Override
    public void addTask(Task task) {
        mtask = task;
        tasks.add(mtask);

        FragmentManager fm = this.getSupportFragmentManager();
        fm.popBackStack(getString(R.string.label_todolist_activity), FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    @Override
    public void cancelCreateTask() {
        FragmentManager fm = this.getSupportFragmentManager();
        fm.popBackStack(getString(R.string.label_todolist_activity), FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    @Override
    public void deleteTask(int index) {
        deleteIndex = index;
        if(deleteIndex >=0){
            tasks.remove(deleteIndex);
            FragmentManager fm = this.getSupportFragmentManager();
            fm.popBackStack(getString(R.string.label_todolist_activity), FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }

    }
}