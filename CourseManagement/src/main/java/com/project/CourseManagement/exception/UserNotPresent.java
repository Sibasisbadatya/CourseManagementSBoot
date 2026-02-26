package com.project.CourseManagement.exception;

public class UserNotPresent extends RuntimeException {
    public UserNotPresent(String message) {
        super(message);
    }
}
