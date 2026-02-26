package com.project.CourseManagement.controller;

import com.project.CourseManagement.dto.AssignmentDTO;
import com.project.CourseManagement.dto.CustomResponse;
import com.project.CourseManagement.entity.User;
import com.project.CourseManagement.service.AssignmentService;
import com.project.CourseManagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;

    private static final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<CustomResponse> uploadAssignment(
            @RequestParam Long courseId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("audio") MultipartFile audio,
            Authentication authentication) throws IOException {
        System.out.println("AUTHENTICATION" + authentication);
        logger.info("AUTHENTICATION INFO" + authentication);
        User user = (User) authentication.getPrincipal();
        AssignmentDTO assignmentDTO = assignmentService.uploadAssignment(courseId, file, audio, authentication.getName());
        CustomResponse customResponse = new CustomResponse("Assignment uploaded successfully", assignmentDTO, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }

    @GetMapping("/{assignmentId}/download")
    public ResponseEntity<CustomResponse> getUploadedAssignment(
            @PathVariable Long assignmentId,
            @RequestParam String type,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        try {
            return assignmentService.getAssignment(assignmentId, type);
        } catch (IOException e) {
            System.out.println("EXCEPTION >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/approve-assignment")
    public ResponseEntity<CustomResponse> approveAssignment(@RequestParam String assignmentId,Authentication authentication){

        AssignmentDTO assignmentDTO = assignmentService.approveAssignment(assignmentId,authentication);
        CustomResponse customResponse = new CustomResponse("Assignment Approved",assignmentDTO,HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }
}
