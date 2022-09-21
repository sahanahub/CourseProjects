package com.example.hw05;

/*
Assignment #: Homework 05
File Name: Posts.java
Full Name of Student: Sahana Srinivas
 */

public class Posts {
    String userName,uID,pID, pText, pDateTime;

    public Posts(){

    }

    public Posts(String userName, String uID, String pID, String pText, String pDateTime) {
        this.userName = userName;
        this.uID = uID;
        this.pID = pID;
        this.pText = pText;
        this.pDateTime = pDateTime;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "userName='" + userName + '\'' +
                ", uID='" + uID + '\'' +
                ", pID='" + pID + '\'' +
                ", pText='" + pText + '\'' +
                ", pDateTime='" + pDateTime + '\'' +
                '}';
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getpText() {
        return pText;
    }

    public void setpText(String pText) {
        this.pText = pText;
    }

    public String getpDateTime() {
        return pDateTime;
    }

    public void setpDateTime(String pDateTime) {
        this.pDateTime = pDateTime;
    }
}
