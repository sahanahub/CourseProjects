package com.example.class09;

/*
Assignment #: InClass09
File Name: Forum.java
Full Name of Student1: Sahana Srinivas
Full Name of Student2: Krithika Kasaragod
 */

public class Forum {
    String DID, UID,forumTitle, forumDescription, forumCreator, forumDateTime;


    public Forum(){

    }



    public Forum(String DID, String UID, String forumTitle, String forumDescription, String forumCreator, String forumDateTime) {
        this.DID = DID;
        this.UID = UID;
        this.forumTitle = forumTitle;
        this.forumDescription = forumDescription;
        this.forumCreator = forumCreator;
        this.forumDateTime = forumDateTime;

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
        return "Forums{" +
                "UID='" + UID + '\'' +
                ", forumTitle='" + forumTitle + '\'' +
                ", forumDescription='" + forumDescription + '\'' +
                ", forumCreator='" + forumCreator + '\'' +
                ", forumDateTime='" + forumDateTime + '\'' +
                '}';
    }
}
