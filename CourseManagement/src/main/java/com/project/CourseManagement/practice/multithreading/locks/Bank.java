package com.project.CourseManagement.practice.multithreading.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Account implements Runnable {
    private int balance = 100;

    public synchronized void withdrawl(int x) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "is attemting to withdrawl");
        if (x > balance) {
            System.out.println("Insufficient Money for " + Thread.currentThread().getName() + " to withdraw.");
        } else {
            Thread.sleep(2000);
            balance -= x;
            System.out.println("Your Current Amount of " + Thread.currentThread().getName() + " is " + balance);
        }
    }

    @Override
    public void run() {
        try {
            withdrawl(60);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class LockedAccount implements Runnable {
    private int balance = 100;



    private final Lock lock = new ReentrantLock();
//    private final Lock lock = new ReentrantLock(true); //normally if t1 is locked then t2 comes and then t3 but somehow t3 gets lock after t1 to give fairness we write true
//    🧠 Real-Life Analogy
//🔐 lock()
//    You stand in line at ATM.
//    Even if 20 people are ahead, you wait.

// ⚡ tryLock()
//    You check ATM:
//    If free → use it.
//    If busy → leave.
//⏳ tryLock(timeout)
//    You wait 2 minutes.
//    If still busy → leave.

    public void withdrawl(int x) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "is attemting to withdrawl");
        try {
//            lock.lock(); does the same as synchronized


//            lock.lockInterruptibly()
//            ----------------------------
//            lock() ignores interrupt while waiting.
//
//            NORMAL FLOW
//            Thread A gets the lock.
//            Thread B tries to get the lock.
//            Lock is busy → B waits.
//
//            Now someone calls:
//            B.interrupt();
//            ❗ What happens?
//
//          👉 Nothing happens to B.
//          👉 B keeps waiting.
//          👉 It will only run when A releases the lock.
//                    So:
//            lock() ignores interrupt while waiting.

//            WITH INTERUPTIBILITY()
//            What happens now?
//
//          👉 B immediately stops waiting.
//          👉 InterruptedException is thrown.
//          👉 B exits.
            System.out.println(Thread.currentThread().getName());
            if (lock.tryLock(100, TimeUnit.MILLISECONDS)) { //only .tryLock() returs true or false if not available but with time parameter it waits for give time.
                //here with time parameter we should use try catch because with that specified time there might be interruptException

                if (x > balance) {
                    System.out.println("Insufficient Money for " + Thread.currentThread().getName() + " to withdraw.");
                } else {
                    try {
                        Thread.sleep(2000);
                        balance -= x;
                        System.out.println("Your Current Amount of " + Thread.currentThread().getName() + " is " + balance);
                    } catch (Exception e) {
                        Thread.currentThread().interrupt(); //good practice
                        throw new RuntimeException(e);
                    } finally {
                        lock.unlock();
                    }
                }

            } else {
                System.out.println(Thread.currentThread().getName() + "Couldn't acquire lock try again later");
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt(); //good practice basically to make state INTERRUPTED so that in future we can do cleanup something
            throw new RuntimeException(e);
        }
        if(Thread.currentThread().isInterrupted()){
            System.out.println("Clean Up Code Here");
            //can do clean up functions since the thread is interrupted
        }
    }

    @Override
    public void run() {
        try {
            withdrawl(60);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class Bank {

    public static void main(String[] args) {
//        Account a1 = new Account();
        LockedAccount a1 = new LockedAccount();
        Thread t1 = new Thread(a1, "A");
        Thread t2 = new Thread(a1, "B");
        t1.start();
        t2.start();


    }

}
