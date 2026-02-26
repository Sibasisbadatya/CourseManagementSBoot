package com.project.CourseManagement.practice.stream;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TerminalOperations {
    public static <Char> void main(String[] args) {
        List<String> slist = Arrays.asList("Sibasis", "Piyush", "Ayush", "Shreyas");
        List<Integer> nlist = Arrays.asList(1, 3, 5, 7);
//        1.Collect
        System.out.println(nlist.stream().map(x -> x + 5).collect(Collectors.toSet()));
        nlist.stream().map(x -> x + 5).toList();//toList is available in new java version

//        2.For Each
        slist.stream().forEach(x -> System.out.println(x));

//        3.Reduce
        Optional<Integer> reducedValue = nlist.stream().reduce(Integer::sum);
        System.out.println(reducedValue.get());

//        4.Count
        long count = nlist.stream().filter(x -> x % 2 == 0).count();

//        5.AnyMatch ,AllMatch ,NoneMatch
        boolean anyMatch = nlist.stream().anyMatch(x -> x % 2 == 0);
        System.out.println(anyMatch);
        boolean allMatch = nlist.stream().allMatch(x -> x % 2 == 0);
        System.out.println(allMatch);
        boolean noneMatch = nlist.stream().noneMatch(x -> x % 2 == 0);
        System.out.println(noneMatch);

//        6. FindFirst ,FindAny
        Integer findFirst = nlist.stream().findFirst().get();
        Integer findAny = nlist.stream().findAny().get();

//        Example
        String sentence = "Hello World";
        char[] charArray = sentence.toCharArray();
//        Arrays.stream();  Here in Arrays.stream char array is not supported
        IntStream chars = sentence.chars();//.chars() method generates stream of chars **IntStream** because of ascii code

//      Stateful and Stateless operation

//        stateful example .sorted()
//        stateless example .map()
    }
}
