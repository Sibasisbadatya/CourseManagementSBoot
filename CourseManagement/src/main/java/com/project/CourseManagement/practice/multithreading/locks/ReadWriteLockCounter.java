package com.project.CourseManagement.practice.multithreading.locks;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
class Counter {
    private int count = 0;
    private final ReadWriteLock lock = new ReentrantReadWriteLock(); // This lock allows multiple threads to read until one tries to write
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public void getCount(String name) {
        readLock.lock();
        System.out.println(name + " is Reading");

        try {
            Thread.sleep(1000);
            System.out.println("Count is " + count);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
        } finally {
            readLock.unlock();
        }

    }

    public void increment(String name) {
        writeLock.lock();
        System.out.println(name + " is Writting");
        try {
            count++;
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
        } finally {
            writeLock.unlock();
        }
    }

}

public class ReadWriteLockCounter {
    //    Add add = new Add();
//        System.out.println(add.operate(5, 10));
//    MathOperation addOperation = ((a, b) -> a + b);
//    Counter counter = new Counter();
//    Thread readThread = (()->counter.get)
    public static void main(String[] args) {
        Counter counter = new Counter();
        new Thread(() -> counter.getCount("Thread1")).start();
        new Thread(() -> counter.getCount("Thread2")).start();
        new Thread(() -> counter.getCount("Thread3")).start();
        new Thread(() -> counter.increment("Thread4")).start();

    }

}
