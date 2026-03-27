package com.project.CourseManagement.practice.multithreading.Synchronised;

public class Counter {
    private int count;
    public synchronized void  increment(){//synchronise din function level
//        OR make synchronised in block level
        synchronized (this){ //this is for the instance for whcih multiple threads are executing
            count++;
        }
//this code block where multiple thread access or mofifying same resource called critical section
//        Here Race condition may applies if sunchronised() not applies
//        after synchronised this section becomes mutual exclusion
    }
    public int getCount(){
        return count;
    }
}
