package com.project.CourseManagement.exception;

public class OutOfCapacity extends RuntimeException {
    public OutOfCapacity(String message) {
        super(message);
    }
}
