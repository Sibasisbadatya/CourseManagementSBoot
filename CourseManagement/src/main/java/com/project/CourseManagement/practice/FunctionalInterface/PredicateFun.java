package com.project.CourseManagement.practice.FunctionalInterface;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class PredicateFun {
    public static void main(String[] args) {
        Predicate<Integer> isEven = x -> x % 2 == 0;
        Predicate<String> isStartsWithA = str -> str.toLowerCase().startsWith("A");
        Predicate<String> isEndsWithA = str -> str.toLowerCase().endsWith("A");
//        isEven.test(9);
        System.out.println(isStartsWithA.test("Ankita"));
        Predicate<String> and = isStartsWithA.and(isEndsWithA);
        System.out.println(and.test("Ankita"));
    }
}


//There also exist BiPredicate ,BiConsumer,BiFunction,UnaryOperator,BinaryOperator also if time permits check those out.
