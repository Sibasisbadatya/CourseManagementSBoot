package com.project.CourseManagement.practice.stream;

import java.lang.reflect.Array;
import java.util.*;
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
        System.out.println("findFirst"+findFirst);
        Integer findAny = nlist.stream().findAny().get();
        System.out.println("findAny"+findAny);


//        7 toArray
        Object[] array = nlist.stream().toArray();

//      8.min max
        Optional<Integer> max = Stream.of(1, 2, 3, 4, 5).max((a, b) -> a - b);
        Optional<Integer> max1 = Stream.of(1, 2, 3, 4, 5).max(Comparator.naturalOrder());

//        9.FlatMap

        List<List<String>> names = Arrays.asList(
                Arrays.asList("Ram", "Shyam"),
                Arrays.asList("Amit", "John")
        );
        List<String> list = names.stream().flatMap(Collection::stream).map(String::toUpperCase).toList();
//        Here the code like flatMap(Collection::stream) equivalent to list->list.stream()
        System.out.println(list);


//        10.For Each Ordered
//        In parallel stream for forEach, element will come arbitrarily for ordered forEachOrdered will be used



//        Example
        String sentence = "Hello World";
        char[] charArray = sentence.toCharArray();
//        Arrays.stream(charArray);  Here in Arrays.stream char array is not supported
        IntStream chars = sentence.chars();//.chars() method generates stream of chars **IntStream** because of ascii code

//      Stateful and Stateless operation

//        stateful example .sorted()
//        stateless example .map()
//        once terminal operation used it cant be again operated like for intermediate operations
    }
}
