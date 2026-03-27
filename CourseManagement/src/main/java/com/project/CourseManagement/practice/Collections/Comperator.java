package com.project.CourseManagement.practice.Collections;

import java.util.*;

class Student {
    int age;
    String name;
    double marks;

    Student(int age, String name, double marks) {
        this.age = age;
        this.name = name;
        this.marks = marks;
    }

    int getAge() {
        return this.age;
    }

    String getName() {
        return this.name;
    }

    double getMarks() {
        return this.marks;
    }

    public String toString() {
        return name + " " + age + " " + marks;
    }
}

class StudentComparator implements Comparator<Student> {
    public int compare(Student s1, Student s2) {
        if (s1.marks != s2.marks) {
            return Double.compare(s2.marks, s1.marks); // descending
        }
        if (s1.age != s2.age) {
            return s1.age - s2.age;
        }
        return s1.name.compareTo(s2.name);
    }
}

public class Comperator {

    public static void main(String[] args) {

        List<Student> list = new ArrayList<>();

        list.add(new Student(22, "Ram", 85));
        list.add(new Student(20, "Alex", 90));
        list.add(new Student(22, "John", 85));

        Collections.sort(list,new StudentComparator()); // It's for custom comparator

        list.sort(
                Comparator.comparing((Student s) -> s.marks).reversed()
                        .thenComparing(s -> s.age)
                        .thenComparing(s -> s.name)
        );
//        this is kinda messy

        Comparator<Student> comparator = Comparator.comparing(Student::getAge).thenComparing((Student::getMarks)).reversed().thenComparing(Student::getName);
        list.sort(comparator);

//        its very simple after creating one comparator independently
        System.out.println(list);
    }
}