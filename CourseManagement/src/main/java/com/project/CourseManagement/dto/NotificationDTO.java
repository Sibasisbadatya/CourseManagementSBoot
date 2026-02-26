package com.project.CourseManagement.dto;

import java.time.LocalDateTime;

public class NotificationDTO<T> {
    private LocalDateTime timestamp;
    private String message;
    private T data;
    private int statusCode;

    public NotificationDTO(String message, T data, int statusCode) {
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }
}
