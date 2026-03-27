package com.project.CourseManagement.practice.multithreading;

public class Basic {
    public static void main(String[] args) {
        System.out.println("Hello World");
        System.out.println(Thread.currentThread().getName());
//        To create thread class we should either extend thread class or implements Runnable interface

        PrintTh print = new PrintTh();// New state of a thread
        print.start(); //Runnable state
//      When run method start executing it will be in Running state But there is no state called Running like ist hypothetical
//      Above is for extending thread class
//      Blocked/Waiting a thread is waiting for a resource or another thread to perform an action
//        After end it goes to Terminated State
        PrintRun printRun = new PrintRun();
        Thread thread = new Thread(printRun); // here we are using runnable instance to initiate thread

        thread.start();
//        Above is for to implement thread using runnable interface


        //start method id used to called to initiate the thread .
        for (; ; ) {
            System.out.println("Sibasis ");
        }
    }
}
