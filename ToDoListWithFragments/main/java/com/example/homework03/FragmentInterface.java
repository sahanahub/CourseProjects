package com.example.homework03;

//Homework03
//FragmentInterface.java
//Sahana Srinivas

public interface FragmentInterface {

    void createTask();
    void viewTask(Task task, int deleteIndex);
    void addTask(Task task);
    void cancelCreateTask();
    void deleteTask(int index);

}
