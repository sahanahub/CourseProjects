package com.example.inclass03;

import java.io.Serializable;

public class ProfileClass implements Serializable {
    String name;
    String email;
    int id;
    String Dept;

    public ProfileClass(String name, String email, int id, String dept) {
        this.name = name;
        this.email = email;
        this.id = id;
        Dept = dept;
    }
  }
