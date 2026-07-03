package com.project.CourseManagement.practice.Collections.LinkedList;

import java.util.LinkedList;
import java.util.Stack;

public class LList {
    public static void main(String[] args) {
        LinkedList<Integer>list = new LinkedList<>();
        list.add(5);
        list.add(10);
        System.out.println(list);
        list.addLast(15);
        list.addFirst(1);
        System.out.println(list);

    }
}
