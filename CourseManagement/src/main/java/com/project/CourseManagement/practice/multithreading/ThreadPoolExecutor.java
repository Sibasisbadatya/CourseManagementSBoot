package com.project.CourseManagement.practice.multithreading;

import org.aspectj.weaver.ast.Call;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;


public class ThreadPoolExecutor {
//    A thread pool is a group of pre-created threads that are reused to execute tasks.
//Instead of:
//            ❌ Creating new thread every time (slow + heavy)
//            ✅ Reusing existing threads (fast + efficient)


    public void simpleThreading() {
        long startTime = System.currentTimeMillis();
        for (int i = 1; i <= 10; i++) {
            int num = i;
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task running " + num);
            }).start();
        }
        System.out.println("Total Time" + (System.currentTimeMillis() - startTime)); //Here before threa start only this statement got printed because we didn't wait for all thread  to complete

    }


    public void threadingWithThreadArraysAndWaitingForAll() throws InterruptedException {
        //        Code to wait all threads
        long startTime = System.currentTimeMillis();
        Thread[] threads = new Thread[10];
        for (int i = 1; i <= 10; i++) {
            int num = i;
            threads[i - 1] = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task running " + num);
            });
            threads[i - 1].start();
        }
        for (Thread t1 : threads) {
            t1.join();
        }
        System.out.println("Total Time" + (System.currentTimeMillis() - startTime));
        //Here this line will wait for all thread to complete
    }

    public void threadingWithExecutor() throws InterruptedException {
        //        With Executor
        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 10; i++) {
            int num = i;
            executor.submit(() -> {
                System.out.println("Task running " + num);
            });
//             What submit() does
//            👉 It takes a task (Runnable or Callable)
//            👉 Sends it to the thread pool, and it executes in background
//            👉 Returns a Future object (very important) ->Think of it like a promise of result in future
//            e.g. Future<?> f = executor.submit(task); can check if task is done,get result,cancel task

//            It is designed around Callable (returning result)
//              Even when you pass a Runnable, internally it is converted to something like a Callable

//            submit()
//            There are 3 main forms:
//                     Future<?> submit(Runnable task)
//                    <T> Future<T> submit(Callable<T> task)
//                    <T> Future<T> submit(Runnable task, T result) and in runnable run doesnot return anything
//            Case 1: You pass Runnable
//            executor.submit(() -> {
//                System.out.println("Hello");
//            });
//            Runnable has:
//            void run(); // no return
//            But submit() must return a Future
//            So internally Java wraps it like:
//            Callable<Object> c = () -> {
//                task.run();
//                return null;
//            };

//            Internally everything behaves like Callable

//            In Case 2 passing callable(functional interface having absravt call() method which retuns value) it behaves in normal way

//            Runnable doesnot throws any checked Exception(throws ...) but callble does
//            **********************************
//            If any doubt ask chat gpt for callbale and runnable in executor service in Thread
//            *********************************************
        }

        executor.shutdown();
//        👉 It stops accepting new tasks again no .submit(()->  will execute
//        👉 But continues executing already submitted tasks

//        executor.shutdownNow(); it shutdowns at spot moment
        try {
            executor.awaitTermination(100, TimeUnit.MILLISECONDS); // if we want to reduce time for either execution or waiting then increase the no of newFixedThreadPool
//            👉 It blocks (waits) the current thread
//            👉 Until either:
//            All tasks are completed ✅
//            Timeout is reached ⏱️
//            returns boolean value
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Completed");
    }


    public void executorWithFuture() throws ExecutionException, InterruptedException {
        //        With Executor
        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> futureResult = executor.submit(() -> 42); //not neccesary that always it will return a result  Future<?> futureResult = executor.submit(() -> sout("sibasis"));
//        here for submit returns the future(for non returning functions in thread implementation)
        if (futureResult.isDone()) {
            System.out.println("finished task");
        }
        System.out.println(futureResult.get()); //Waits if necessary for the computation to complete, and then retrieves its result.
        executor.shutdown();

    }

    public void executorWithRunnableAndTask() throws ExecutionException, InterruptedException {
        //        With Executor
        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newSingleThreadExecutor();
//        ExecutorService executorService = Executors.newCachedThreadPool();
        //internally cached thread pool does
//        new ThreadPoolExecutor(
//                0,                      // corePoolSize
//                Integer.MAX_VALUE,      // maxPoolSize (unbounded ⚠️)
//                60L, TimeUnit.SECONDS,  // idle thread timeout
//                new SynchronousQueue<>() // no queue
//        );

//        ✅ 1. Creates threads on demand
//        If no idle thread → creates new thread
//        ✅ 2. Reuses idle threads
//                If thread is free → reused
//        ✅ 3. Threads die after 60 sec idle
//                Saves memory/resources
//        ⚠️ 4. Unlimited threads (danger)
//                        Can go up to Integer.MAX_VALUE
//                Risk: OutOfMemoryError
//        Slightly slower under heavy load

        Future<String> submit = executor.submit(() -> System.out.println("hello"), "success");
        if (submit.isDone()) {
            System.out.println("finished task");
        }
        //we can use .isCancelled() also if we done future.cancel(true); for some reason for .cancel(false) thread will continue running but status of future will be cancelled
        System.out.println(submit.get()); //Waits if necessary for the computation to complete, and then retrieves its result.
//        submit.get(1000,TimeUnit.MILLISECONDS); If done → return result ✅  If not → throw TimeoutException ❌
        executor.shutdown();
    }

    public void multiExecutorService() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Callable<Integer> callable1 = () -> {
            System.out.println("Hello 1");
            return 1;
        };
        Callable<Integer> callable2 = () -> 2;
        Callable<Integer> callable3 = () -> {
            System.out.println("Hello 3");
            return 3;
        };
        List<Callable<Integer>> list = Arrays.asList(callable1, callable2, callable3);
//        List<Future<Integer>> futures = executorService.invokeAll(list);
        List<Future<Integer>> futures = executorService.invokeAll(list, 1000, TimeUnit.MILLISECONDS); //it waits given time it will not execute the thread which was invoked after that second
//        What it does ? (simple)
//        Takes a list of Callable tasks
//        Submits all tasks at once
//        Waits (blocks) until all tasks are finished
//        Returns a List of Future objects wrapped inside Future.get()

        for (Future<Integer> f : futures) {
            System.out.println(f.get());
        }

        System.out.println("Done");
    }

    public void multiExecutorServiceAny() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Callable<Integer> callable1 = () -> {
            System.out.println("Hello 1");
            return 1;
        };
        Callable<Integer> callable2 = () -> 2;
        Callable<Integer> callable3 = () -> {
            System.out.println("Hello 3");
            return 3;
        };
        List<Callable<Integer>> list = Arrays.asList(callable1, callable2, callable3);
        Integer i = executorService.invokeAny(list);

        System.out.println(i);

        System.out.println("Done");
    }

    public void scheduledExecutorService() throws InterruptedException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.schedule(
//                () -> System.out.println("Task executed after 5 seconds daily"),
//                5,
//                TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(
                () -> System.out.println("Task executed after 5 seconds daily"),
                2,
                2,
                TimeUnit.SECONDS); // here this periodic task will run after some dealy so we should handle.shutdown carefully

        scheduler.scheduleAtFixedRate(
                () -> System.out.println("Task executed after 5 seconds daily"),
                2,
                2,
                TimeUnit.SECONDS);//it waits for delay sec after previous task completes but scheduleAtFixedRate will trigger next task after given time
        // doesn't depends upon completionof previous tasks

        Thread.sleep(5000); // here we do sleep to execute the scheduleAtFixedRate otherwise .shutdown() executes and no task will run , and we can create a new scheduler inside which we can shutdown
        scheduler.shutdown();
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException {


        ThreadPoolExecutor t1 = new ThreadPoolExecutor();
//    t1.threadingWithExecutor();
//        t1.executorWithFuture();
//        t1.executorWithRunnableAndTask();
//        t1.multiExecutorService();
//        t1.multiExecutorServiceAny();
        t1.scheduledExecutorService();

    }
}
