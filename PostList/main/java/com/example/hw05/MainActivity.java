package com.example.hw05;
/*
Assignment #: Homework 05
File Name: MainActivity.java
Full Name of Student: Sahana Srinivas
 */

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements IFragment {

    public static final String FILE_NAME = "com.example.hw05_sharedPreferenceFile";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    String muserId, muserName, mtoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        myEdit.putString(getString(R.string.user_id),muserId);
        myEdit.putString(getString(R.string.username), muserName);
        myEdit.putString(getString(R.string.token), mtoken);
        myEdit.apply();

        if (sharedPreferences.contains(getString(R.string.token))){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerMain, new PostListFragment(), getString(R.string.label_tagPostListFragment))
                    .commit();
        } else{
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerMain, new LoginFragment(), getString(R.string.label_tagLoginFragment))
                    .commit();
        }

    }

    @Override
    public void goToCreateAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new RegisterFragment(), getString(R.string.label_register_fragment_title))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToPostsList(String userid, String username, String token) {
        muserId = userid;
        muserName = username;
        mtoken = token;
        sharedPreferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        myEdit.putString(getString(R.string.user_id),userid);
        myEdit.putString(getString(R.string.username), username);
        myEdit.putString(getString(R.string.token), token);
        myEdit.apply();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new PostListFragment(), getString(R.string.label_tagPostListFragment))
                .commit();
    }

    @Override
    public void goToLogin() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void logOutUser() {
        myEdit.clear();
        myEdit.apply();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new LoginFragment(), getString(R.string.label_tagLoginFragment))
                .commit();
    }

    @Override
    public void goToCreatePost() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new CreatePostFragment(), getString(R.string.label_tagCreatePostFragment))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToPostListfromCreate() {
        getSupportFragmentManager().popBackStack();
    }

}