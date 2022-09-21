package com.example.inclass06;
/*
a. Assignment #:InClass06
b. File Name:HeavyWork.java
c. Full name of the Student 1: Krithika Kasaragod
c. Full name of the Student 2: Sahana Srinivas
*/

import java.util.Random;


public class HeavyWork {
	public static final long DELAY_MILLI_SECS = 2000;

	public static double getNumber(){
		addSomeDelay(DELAY_MILLI_SECS);
		Random rand = new Random();
		return rand.nextDouble();
	}

	private static void addSomeDelay(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}