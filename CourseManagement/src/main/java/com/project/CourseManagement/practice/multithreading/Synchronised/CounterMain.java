package com.project.CourseManagement.practice.multithreading.Synchronised;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CounterMain {
    public static void main(String[] args) {
        Counter counter = new Counter();
        MyThread t1 = new MyThread(counter);
        MyThread t2 = new MyThread(counter);
        t1.start();
        t2.start();
        try{
            t1.join();
            t2.join();
        }
        catch (Exception e){
            log.info("Error {}",e.getMessage());
        }
        System.out.println(counter.getCount());
    }

}
