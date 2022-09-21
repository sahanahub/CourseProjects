package com.example.homework06;

/*
Assignment #: Homework06
File Name: Forum.java
Full Name of Student1: Sahana Srinivas
 */

import java.util.ArrayList;

public class Forum {
    String DID, UID,forumTitle, forumDescription, forumCreator, forumDateTime;
    ArrayList<String> likedUsers;


    public Forum(){

    }


    public Forum(String DID, String UID, String forumTitle, String forumDescription, String forumCreator, String forumDateTime, ArrayList<String> likedUsers) {
        this.DID = DID;
        this.UID = UID;
        this.forumTitle = forumTitle;
        this.forumDescription = forumDescription;
        this.forumCreator = forumCreator;
        this.forumDateTime = forumDateTime;
        this.likedUsers = likedUsers;
    }

    public ArrayList<String> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(ArrayList<String> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public String getDID() {
        return DID;
    }

    public void setDID(String DID) {
        this.DID = DID;
    }

    public String getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }

    public String getForumDescription() {
        return forumDescription;
    }

    public void setForumDescription(String forumDescription) {
        this.forumDescription = forumDescription;
    }

    public String getForumCreator() {
        return forumCreator;
    }

    public void setForumCreator(String forumCreator) {
        this.forumCreator = forumCreator;
    }

    public String getForumDateTime() {
        return forumDateTime;
    }

    public void setForumDateTime(String forumDateTime) {
        this.forumDateTime = forumDateTime;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }


    @Override
    public String toString() {
        return "Forum{" +
                "DID='" + DID + '\'' +
                ", UID='" + UID + '\'' +
                ", forumTitle='" + forumTitle + '\'' +
                ", forumDescription='" + forumDescription + '\'' +
                ", forumCreator='" + forumCreator + '\'' +
                ", forumDateTime='" + forumDateTime + '\'' +
                ", likedUsers=" + likedUsers +
                '}';
    }
}
