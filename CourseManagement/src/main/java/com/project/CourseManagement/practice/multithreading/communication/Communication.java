package com.project.CourseManagement.practice.multithreading.communication;

//Thread communication means threads coordinate with each other by sending signals so that they work in the correct order.
//Without proper communication mechanism,threads might end up in inefficient busy-waiting states,
// leading to wastage of CPU resources and potential deadlocks.
//methods for communication
// wait
// notify
// notifyAll this method can be called in synchronised context


//Without communication, threads may:
//
//Waste CPU time by continuously checking conditions (busy waiting).
//Access resources before they are ready.
//Produce incorrect results due to improper execution order.


//wait() makes the current thread pause execution and release the lock until another thread notifies it. JVM stores where the thread stopped
//notify() wakes up one thread that is waiting on the object’s monitor.the thread continues from the next line after wait().
//notifyAll() wakes up all threads waiting on that object's monitor. but only one thread get the lock first


//Sometimes one thread depends on another thread’s work.
//Example:
//
//    Producer → produces data
//    Consumer → consumes data
//
//But if the consumer runs before the producer, there is no data.
//So the consumer should wait, and when the producer produces data it should notify the consumer.

class Food {

    private boolean available = false;

    public synchronized void cookFood() {
        System.out.println("Chef is cooking food...");
        available = true;
        System.out.println("Food is ready!");
        notify(); // wake up waiting thread
    }

    public synchronized void serveFood() {
        if (!available) {
            try {
                System.out.println("Delivery boy waiting for food...");
                wait(); // wait until food is ready
            } catch (Exception e) {}
        }
        System.out.println("Delivery boy took the food");
    }
}

public class Communication {
    public static void main(String[] args) {

        Food food = new Food();
        Thread delivery = new Thread(() -> {
            food.serveFood();
        });

        Thread chef = new Thread(() -> {
            try { Thread.sleep(2000); } catch(Exception e) {}
            food.cookFood();
        });
        delivery.start();
        chef.start();
    }
}
