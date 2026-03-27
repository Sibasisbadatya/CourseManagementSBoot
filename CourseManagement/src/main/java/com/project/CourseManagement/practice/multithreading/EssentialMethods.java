package com.project.CourseManagement.practice.multithreading;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EssentialMethods extends Thread {

    EssentialMethods(String name) {
        super(name);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " priority" + Thread.currentThread().getPriority() + " count" + i);
                Thread.yield();
//                `Thread.yield()` temporarily hints the scheduler to pause the current thread and give other runnable threads a chance to execute.
            }
        } catch (InterruptedException e) {
            log.info("Error {}", e.getMessage());
        }
//        System.out.println("Thread is Running");
    }

    public static void main(String[] args) throws InterruptedException {

//      By using constructor in this class we can name to the thread
        EssentialMethods t1 = new EssentialMethods("LOW");
        EssentialMethods t2 = new EssentialMethods("MEDIUM");
        EssentialMethods t3 = new EssentialMethods("HIGH");
//      Setting higher priority doesn't mean that always high priority thread wil come into process

        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.NORM_PRIORITY);
        t3.setPriority(Thread.MAX_PRIORITY);


        t1.setDaemon(true);
        t1.start();
//        If multiple threads are running and one is daemon and another is non-daemon, then the main thread waits for the non-daemon thread to complete,
//                and after that JVM stops, ignoring daemon threads running in the background.
        System.out.println("Thread Done");
        t2.start();
//        t3.start();
//        t3.interrupt();


//        t1.join();
//        System.out.println("Hello");

//        DAEMON THREAD which run in background for which jvm doesn't wait

    }
}
