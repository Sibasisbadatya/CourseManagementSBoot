package com.project.CourseManagement.practice.stream;

import java.util.*;
import java.util.stream.Collectors;

public class CollectorsFun {
    public static void main(String[] args) {
        List<String> slist = Arrays.asList("Sibasis", "Piyush", "Ayush", "Shreyas","Sibasis");
//        1.To List
        List<String> clist = slist.stream().collect(Collectors.toList());
//        2.To Set
        Set<String> ulist = slist.stream().collect(Collectors.toSet());
//        3. To Collections

        ArrayDeque<String>  adlist = slist.stream().collect(Collectors.toCollection(() -> new ArrayDeque<>()));
//        .toCOllection expect a supplier which takes no arg but give something

//        4.Joining
//        Concatenates stream elements to a single string

        String collect = slist.stream().collect(Collectors.joining(","));
        String collect1 = slist.stream().collect(Collectors.joining());
        System.out.println(collect1);
        System.out.println(collect);
//      5.Summarising Data
//        Statistical Data (sum,total,min,average,max)
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        IntSummaryStatistics collect2 = list.stream().collect(Collectors.summarizingInt(x -> x));
//        Here from collect2 we can get like .getCOunt() .getAverage() etc

//        6.Calculating Average
        Double collect3 = list.stream().collect(Collectors.averagingInt(x -> x));
        System.out.println(collect3);
//        7.COunting elements

//        8 .GroupBy
        Map<Integer, List<String>> groupList = slist.stream().collect(Collectors.groupingBy(name -> name.length()));
        Map<Integer, String> glistcolcted = slist.stream().collect(Collectors.groupingBy(name -> name.length(), Collectors.joining(",")));

//        In second example what happens is 1st param,eter of colect returns array and for that array we again collect
//        to gain final result
        Map<Integer, Long> glistcolctedcount = slist.stream().collect(Collectors.groupingBy(name -> name.length(), Collectors.counting()));
        System.out.println(glistcolctedcount);
//      9.Partitioning elements
//        partiotioning elements based on groups

        Map<Boolean, List<Integer>> collect4 = list.stream().collect(Collectors.partitioningBy(x -> x % 2 == 0));
        System.out.println(collect4);
    }


}
