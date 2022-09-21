package com.example.homework06;

/*
Assignment #: Homework06
File Name: Comments.java
Full Name of Student1: Sahana Srinivas
 */

public class Comments {
    String forumDocId, docId, userName, userId, commentText, dateTime;

    public Comments(){

    }

    public Comments(String forumDocId, String docId, String userName, String userId, String commentText, String dateTime) {
        this.forumDocId = forumDocId;
        this.docId = docId;
        this.userName = userName;
        this.userId = userId;
        this.commentText = commentText;
        this.dateTime = dateTime;
    }

    public String getForumDocId() {
        return forumDocId;
    }

    public void setForumDocId(String forumDocId) {
        this.forumDocId = forumDocId;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "forumDocId='" + forumDocId + '\'' +
                ", docId='" + docId + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", commentText='" + commentText + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
