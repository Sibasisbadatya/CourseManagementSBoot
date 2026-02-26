package com.project.CourseManagement.practice.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class IntermediateOperations {


    public static void main(String[] args) {
        //    Intermediate operations convert one stream to another stream
//    They are lazy meaning they don't execute until terminal operation is invoked.
        List<String> list = Arrays.asList("Sibasis", "Piyush", "Ayush", "Shreyas");
//        FILTER
        Stream<String> streamlist = list.stream().filter(x -> x.startsWith("S"));
//    No filtering at this point since no terminal operation is invoked
        long count = streamlist.count();
        System.out.println(count);

//        MAP
        Stream<String> streamlist2 = list.stream().map(String::toUpperCase);//methos refernce used here

//        sorted
        Stream<String> sorted = list.stream().sorted();
        Stream<String> customSorted = list.stream().sorted((a, b) -> a.length() - b.length());
//        Distinct
        System.out.println(list.stream().filter(x -> x.startsWith("S")).distinct().count());
//        LIMIT
        Stream.iterate(1, x -> x + 1).limit(100);
//        SKIP
        Stream.iterate(0,x->x+1).skip(10).limit(20).forEach(System.out::println);
    }

}
