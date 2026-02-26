package com.project.CourseManagement.practice.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamDemo {

//    process collections of data in a functional and declratrive manner
//    simplify data processing
//    Improve readability
//    Embrace functional programming
//    Enable easy parallelism without multithreading


//    How to use streams
//    1.Source
//    2.Intermediate operations
//    3.Terminal operation

    //    What is Stream?
//    A sequence of elements supporting functional and declarative programming

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println(numbers.stream().filter(x -> x % 2 == 0).count());

//        here source is numbers
//        filter is intermediate process
//        count is terminal process


//        Creating streams
//        From collections
        List<Integer> numbersList = Arrays.asList(1, 2, 3, 4, 5);
        Stream<Integer> stream1 = numbersList.stream();
//        From arrays
        String[] array = {"a", "b", "c"};
        Stream<String> stream2 = Arrays.stream(array);
//        Using Stream.of()
        Stream<Integer> stream3 = Stream.of(1, 2, 3, 4, 5);

//        Infinite streams
        Stream<Integer> stream4 = Stream.generate(() -> 1);
        Stream<Integer> stream5 = Stream.iterate(0,x->x+1); //seed is strating point
//        Of limited size
        Stream<Integer> stream6 = Stream.generate(() -> 1).limit(100);
//

    }


}
