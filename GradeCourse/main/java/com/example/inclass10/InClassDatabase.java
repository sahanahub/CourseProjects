package com.example.inclass10;

/*
Assignment #: InClass10
File Name: InClassDatabase.java
Full Name of Student1: Sahana Srinivas
Full Name of Student2: Krithika Kasaragod
 */

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Course.class},version = 1)
public abstract class InClassDatabase extends RoomDatabase {

    public abstract CourseDao CDao();

}
