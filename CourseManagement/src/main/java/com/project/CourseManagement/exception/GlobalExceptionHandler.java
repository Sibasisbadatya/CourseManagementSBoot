package com.project.CourseManagement.exception;

import com.project.CourseManagement.dto.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<CustomResponse> userAlreadyExists(UserAlreadyExists userAlreadyExists, WebRequest webRequest) {
        CustomResponse customResponse = new CustomResponse(userAlreadyExists.getMessage(), webRequest.getDescription(false), HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(customResponse);
    }

    @ExceptionHandler(UserRegistrationError.class)
    public ResponseEntity<CustomResponse> userRegistrationError(UserRegistrationError userRegistrationError, WebRequest webRequest) {
        CustomResponse customResponse = new CustomResponse(userRegistrationError.getMessage(), webRequest.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<CustomResponse> fileStorageException(FileStorageException fileStorageException, WebRequest webRequest) {
        CustomResponse customResponse = new CustomResponse(fileStorageException.getMessage(), webRequest.getDescription(false), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }

    @ExceptionHandler(AssignmentNotFound.class)
    public ResponseEntity<CustomResponse> assignmentNotFound(AssignmentNotFound assignmentNotFound, WebRequest webRequest) {
        CustomResponse customResponse = new CustomResponse(assignmentNotFound.getMessage(), webRequest.getDescription(false), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<CustomResponse> internalServerError(InternalServerError internalServerError, WebRequest webRequest) {
        CustomResponse customResponse = new CustomResponse(internalServerError.getMessage(), webRequest.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
    }

    @ExceptionHandler(NotificationError.class)
    public ResponseEntity<CustomResponse> notificationError(NotificationError notificationError, WebRequest webRequest) {
        CustomResponse customResponse = new CustomResponse(notificationError.getMessage(), webRequest.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
    }

    @ExceptionHandler(FileNotFound.class)
    public ResponseEntity<CustomResponse> fileNotFound(FileNotFound fileNotFound, WebRequest webRequest) {
        CustomResponse customResponse = new CustomResponse(fileNotFound.getMessage(), webRequest.getDescription(false), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
    }

    @ExceptionHandler(DataNotFound.class)
    public ResponseEntity<CustomResponse> dataNotFound(DataNotFound dataNotFound, WebRequest webRequest) {
        CustomResponse customResponse = new CustomResponse(dataNotFound.getMessage(), webRequest.getDescription(false), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
    }

    @ExceptionHandler(OutOfCapacity.class)
    public ResponseEntity<CustomResponse> dataNotFound(OutOfCapacity outOfCapacity, WebRequest webRequest) {
        CustomResponse customResponse = new CustomResponse(outOfCapacity.getMessage(), webRequest.getDescription(false), HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }

    @ExceptionHandler(UserNotPresent.class)
    public ResponseEntity<CustomResponse> dataNotFound(UserNotPresent userNotPresent, WebRequest webRequest) {
        {
            CustomResponse customResponse = new CustomResponse(userNotPresent.getMessage(), webRequest.getDescription(false), HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.OK).body(customResponse);
        }
    }
}
