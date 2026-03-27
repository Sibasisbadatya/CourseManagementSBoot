package com.project.CourseManagement.practice.multithreading;

public class PrintTh extends Thread {
    @Override
    public void run() {
        for(;;){
            System.out.println("Helllo ");
        }
    }
}
