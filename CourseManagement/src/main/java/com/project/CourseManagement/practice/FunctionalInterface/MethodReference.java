package com.project.CourseManagement.practice.FunctionalInterface;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Person{
    String name;
    Person(String name){
        this.name = name;
    }
}

public class MethodReference {


    // use method without invoking and in place of lambda expression
    public static void main(String[] args) {
        List<String> students = Arrays.asList("Sibasis", "Piyush", "Ayush");
        students.forEach(System.out::println); // this is function refernce

        //constructor reference
        List<String> friends = Arrays.asList("Sibasis", "Piyush", "Ayush");
        friends.stream().map(x-> new Person(x)).collect(Collectors.toList());
        friends.stream().map(Person::new).collect(Collectors.toList());// this is the constructor reference

    }
}
