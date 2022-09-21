package com.example.inclass05;

/*
a. Assignment #:InCLass05
b. File Name:MainActivity.java
c. Full name of the Student 1:Krithika Kasaragod
d. Full name of the Student 2:Sahana Srinivas
*/

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements IListviewInterface {

    String mCatergoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rlContainer,new AppCategoryFragment(),getString(R.string.label_app_category_tag))
                .commit();
    }

    @Override
    public void gotoAppListFragment(String categoryName) {
        mCatergoryName = categoryName;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rlContainer,AppListFragment.newInstance(mCatergoryName),getString(R.string.label_app_category_tag))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoAppDetailsFragment(DataServices.App appObj) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rlContainer,AppDetailsFragment.newInstance(appObj),getString(R.string.label_app_details_tag))
                .addToBackStack(null)
                .commit();
    }
}