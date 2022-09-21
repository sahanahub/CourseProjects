package com.example.homework04;
/*
a. Assignment #:Homework04
b. File Name:SortClass.java
c. Full name of the Student :Sahana Srinivas
*/

import java.util.Comparator;

public class SortClass {

    public static int mod = 1;
    public SortClass(boolean desc) {
        if (desc) mod =-1;
    }

    static Comparator<DataServices.User> stateComparitor() {
        return new Comparator<DataServices.User>() {

            @Override
            public int compare(DataServices.User t1, DataServices.User t2) {
                return mod*(t1.state.compareTo(t2.state));
            }

        };
    }

    static Comparator<DataServices.User> ageComparitor() {
        return new Comparator<DataServices.User>() {

            @Override
            public int compare(DataServices.User t1, DataServices.User t2) {
                if(t1.age > t2.age) {
                    return 1;
                } else if(t1.age < t2.age){
                    return -1;
                }else {
                    return 0;
                }
            }

        };
    }

    static Comparator<DataServices.User> nameComparitor() {
        return new Comparator<DataServices.User>() {

            @Override
            public int compare(DataServices.User t1, DataServices.User t2) {
                return (t1.name.compareTo(t2.name));
            }

        };
    }

}
