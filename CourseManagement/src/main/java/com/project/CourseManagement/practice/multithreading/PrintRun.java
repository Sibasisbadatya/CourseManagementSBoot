package com.project.CourseManagement.practice.multithreading;

public class PrintRun implements Runnable{
    @Override
    public void run() {
        for(;;){
            System.out.println("Helllo ");
        }
    }
}
