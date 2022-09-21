package com.example.hw05;

/*
Assignment #: Homework 05
File Name: IFragment.java
Full Name of Student: Sahana Srinivas
 */
public interface IFragment {
    void goToCreateAccount();
    void goToPostsList(String userid, String username, String token);
    void goToLogin();
    void logOutUser();
    void goToCreatePost();
    void goToPostListfromCreate();
}
