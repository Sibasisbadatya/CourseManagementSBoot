package com.project.CourseManagement.practice.FunctionalInterface;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FucntionPractice {



    public static void main(String[] args) {
        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<Integer, Integer> tripleIt = x -> x * 3;
        System.out.println(doubleIt.apply(10));
        System.out.println(doubleIt.andThen(tripleIt).apply(4));
        System.out.println(doubleIt.compose(tripleIt).apply(4));
    }

}
