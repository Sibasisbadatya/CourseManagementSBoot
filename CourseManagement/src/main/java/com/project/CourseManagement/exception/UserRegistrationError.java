package com.project.CourseManagement.exception;

public class UserRegistrationError extends RuntimeException {
    public UserRegistrationError(String message) {
        super(message);
    }
}
