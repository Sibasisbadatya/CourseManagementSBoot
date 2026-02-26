package com.project.CourseManagement.practice.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LazyEvaluation {
    public static void main(String[] args) {
        List<String> slist = Arrays.asList("Sibasis", "Piyush", "Ayush", "Shreyas");
        List<Integer> nlist = Arrays.asList(1, 3, 5, 7);
        Stream<String> stream = slist.stream()
                .filter(name->{
                    System.out.println(name);
                    return name.length()>5;
                });
        System.out.println("Before terminal Opertaion");
        List<String>result = stream.toList();
        System.out.println("After terminal Opertaion");
        System.out.println("Result {}"+result);
//        First *Before terminal Opertaion* will print instead od sout(name) after stream.filter because intremediate operation will not execute untill
//        terminal are executed
//        Then Names will be printed
//        Then *After terminal Opertaion* will be printed
    }
}
