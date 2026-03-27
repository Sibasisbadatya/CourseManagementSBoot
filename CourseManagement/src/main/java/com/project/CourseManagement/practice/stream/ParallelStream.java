package com.project.CourseManagement.practice.stream;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ParallelStream {
    private static long factorial(int n) {
        long ans = 1;
        for (long i = 2; i < n; i++) {
            ans *= i;
        }
        return ans;
    }

    public static void main(String[] args) {
//        A type of stream that enables parallel processing of elements
//        Allowing multiple threads to process parts of the stream simultaneously
//        This can significantly improve the performance for large data sets
//        Workload is distributed across multiple threads

        long normalStartTime = System.currentTimeMillis();
        List<Integer> list = Stream.iterate(1, x -> x + 1).limit(2000).toList();
        List<Long> factorialList = list.stream().map(ParallelStream::factorial).toList();
        long normalEndTime = System.currentTimeMillis();
        System.out.println("sequential Stream" + (normalEndTime - normalStartTime) + "ms");

//        **************** FOR PARALLEL PROCESSING ***********************************

        long parallelStartTime = System.currentTimeMillis();
        List<Integer> list1 = Stream.iterate(1, x -> x + 1).limit(2000).toList();
        List<Long> factorialList1 = list.parallelStream().map(ParallelStream::factorial).toList();
        long parallelEndTime = System.currentTimeMillis();
        System.out.println("Parallel time" + (parallelEndTime - parallelStartTime) + "ms");
//      Parallel streams are most effective for CPU-intensive or large datasets where tasks are independent(not dependent on result of previous function)

        List<Integer>numbers = Arrays.asList(1,2,3,4,5);
//        int sum = 0;// normal variable cant used in lambda expression ,it should be final or effectively final
        AtomicInteger sum = new AtomicInteger(0);
        List<Integer> cumulativeSumList = numbers.parallelStream().map(sum::getAndAdd).toList();
//        List<Integer> cumulativeSumList = numbers.stream().sequential().map(sum::getAndAdd).toList(); // sequential also can be used for sequential flow
        System.out.println(cumulativeSumList); //Her ethe rsult will not be expected as {1,3,6,10,15}

    }
}
