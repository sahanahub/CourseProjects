package com.example.inclass10;

/*
Assignment #: InClass10
File Name: CourseDao.java
Full Name of Student1: Sahana Srinivas
Full Name of Student2: Krithika Kasaragod
 */

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert
    public void insertAll(Course... course);

    @Delete
    public void delete(Course course);

    @Update
    public void update(Course course);

   @Query("SELECT * FROM COURSE")
    public List<Course> getAll();

    @Query("SELECT * FROM COURSE WHERE id= :id limit 1")
    Course getByID(long id);

}
