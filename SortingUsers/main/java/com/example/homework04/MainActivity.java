package com.example.homework04;
/*
a. Assignment #:Homework04
b. File Name:MainActivity.java
c. Full name of the Student :Sahana Srinivas
*/
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentInterface, SortRecyclerViewAdapter.ISortRecyclerView {
    ArrayList<DataServices.User> muserObjList;
    String mFilterName = "", mSortAttribute = "";
    int mSortCriteria = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        muserObjList = DataServices.getAllUsers();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.rlContainer,new UsersFragment(),getString(R.string.label_tag_users_fragment))
                .commit();
    }

    @Override
    public void goToFilterFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rlContainer,new FilterFragment(),getString(R.string.label_tag_filter_fragment))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToSortFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rlContainer,new SortFragment(),getString(R.string.label_tag_sort_fragment))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToUserFragmentWithFilter(String stateName) {
        mFilterName = stateName;
        UsersFragment userFragment = (UsersFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.label_tag_users_fragment));
        userFragment.filterName = mFilterName;
        getSupportFragmentManager().popBackStack();

    }

    @Override
    public void goToUserFragmentWithSort(String attribute, int criteria) {

        mSortAttribute = attribute;
        mSortCriteria = criteria;
        UsersFragment userFragment = (UsersFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.label_tag_users_fragment));
        userFragment.sortAttribute = attribute;
        userFragment.sortCriteria = criteria;
        getSupportFragmentManager().popBackStack();

        }
 }
