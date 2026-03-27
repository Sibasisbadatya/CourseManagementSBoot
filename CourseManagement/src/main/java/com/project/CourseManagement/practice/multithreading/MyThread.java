package com.project.CourseManagement.practice.multithreading;

//  DESCRIBES THE LIFECYCLE OF THREAD // **************************************

public class MyThread extends Thread{
    @Override
    public void run() {
        System.out.println("Running THread");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyThread myThread = new MyThread();
        System.out.println(myThread.getState());
        myThread.start();
        System.out.println(myThread.getState());
        Thread.sleep(200); // it is checked exception herethread will stop execution for give time interval in milli sec.
        myThread.join();//caller thread should wait the invoked thread to finish,here caller is main and invoked is myThread
        System.out.println(myThread.getState());
    }
}
