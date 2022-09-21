package com.example.homework06;


/*
Assignment #: Homework06
File Name: MainActivity.java
Full Name of Student1: Sahana Srinivas
 */

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements IService {
    public static final String myPreference = "MY_PREFERENCE";
    public static final String creatorName = "creatorName";
    public static final String UID = "UID";

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth= FirebaseAuth.getInstance();
        if(mAuth.getUid()==null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerMain, new LoginFragment(),getString(R.string.loginFragment))
                    .commit();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerMain, new ForumsFragment(),getString(R.string.forumsFragment))
                    .commit();
        }

    }

    @Override
    public void gotoForumsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new ForumsFragment(),getString(R.string.forumsFragment))
                .commit();
    }

    @Override
    public void gotoRegisterFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new RegisterFragment(),getString(R.string.registerFragment))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoPreviousFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoLoginFromForumsFragment() {
        SharedPreferences sharedPreferences = getSharedPreferences(myPreference, MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.clear();
        myEdit.apply();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new LoginFragment(),getString(R.string.loginFragment))
                .commit();
    }

    @Override
    public void gotoNewForumFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new NewForumFragment(),getString(R.string.label_new_forum))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void refreshForumsFragment() {
        ForumsFragment fragment = (ForumsFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.forumsFragment));
        if(fragment!=null){
            fragment.getForumListData();
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToForumDetailFragment(String docId) {
        String documentId = docId;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, ForumDetailFragment.newInstance(documentId),getString(R.string.title_forum_detail))
                .addToBackStack(null)
                .commit();
    }
}