package com.project.CourseManagement.controller;

import com.project.CourseManagement.dto.AssignmentDTO;
import com.project.CourseManagement.dto.CustomResponse;
import com.project.CourseManagement.dto.FileItemDTO;
import com.project.CourseManagement.entity.Assignment;
import com.project.CourseManagement.entity.User;
import com.project.CourseManagement.enums.FileType;
import com.project.CourseManagement.exception.InternalServerError;
import com.project.CourseManagement.service.AssignmentService;
import com.project.CourseManagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.io.IOException;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;

    private static final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }


    @PostMapping("/submit-assignment")
    public ResponseEntity<CustomResponse> submitAssignment(
            @RequestParam Long assignmentId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("audio") MultipartFile audio,
            @RequestParam("video") MultipartFile video,
            Authentication authentication) throws IOException {
//        MultipartFile = a Java object that represents a file sent in an HTTP request (multipart/form-data)
//                | Method                  | Use                        |
//                | ----------------------- | -------------------------- |
//                | `getOriginalFilename()` | file name                  |
//                | `getSize()`             | file size (bytes)          |
//                | `getContentType()`      | MIME type (audio/mp4 etc.) |
//                | `getBytes()`            | file as byte[]             |
//                | `getInputStream()`      | stream                     |
//                | `isEmpty()`             | check if file exists       |

        System.out.println("AUTHENTICATION" + authentication);
        logger.info("AUTHENTICATION INFO" + authentication);
        User user = (User) authentication.getPrincipal();
        AssignmentDTO assignmentDTO = assignmentService.submitAssignment(assignmentId,file, audio, video, authentication.getName());
        CustomResponse customResponse = new CustomResponse("Assignment uploaded successfully", assignmentDTO, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }

    @GetMapping("/get-assignment/{assignmentId}")
    public ResponseEntity<CustomResponse> getAssignment(
            @PathVariable Long assignmentId,
            Authentication authentication
    ) {
        List<FileItemDTO> assignmentFiles = new ArrayList<>();
        try {
            assignmentFiles = assignmentService.getAssignmentByCourseId(assignmentId);
        } catch (Exception e) {
            throw new InternalServerError("Error Ocuured while fetching Assignment Files");
        }
        CustomResponse customResponse = new CustomResponse("Assignments fetched Succesfully",assignmentFiles,HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);

    }

//    @GetMapping("/{assignmentId}/download")
//    public ResponseEntity<CustomResponse> getUploadedAssignment(
//            @PathVariable Long assignmentId,
//            @RequestParam String type,
//            Authentication authentication
//    ) {
//        User user = (User) authentication.getPrincipal();
//        try {
//            return assignmentService.getAssignment(assignmentId, type);
//        } catch (IOException e) {
//            System.out.println("EXCEPTION >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//            throw new RuntimeException(e);
//        }
//    }


    @GetMapping("/getAssignmentData")
    public ResponseEntity<CustomResponse> getAssignmentData(@RequestParam Long assignmentId, @RequestParam FileType type){
        Resource resource = assignmentService.loadFileAsResource(assignmentId, type);
        //In Spring, Resource is a powerful interface used to abstract away where a file actually lives. Whether a file is
        // on your local hard drive, inside a JAR file, or on a remote server, the Resource interface provides a unified way
        // to read it.

//        Implementation,Use Case
//        FileSystemResource,"For files on the local file system (e.g., C:/uploads/video.mp4)."
//        ClassPathResource,"For files inside your project (e.g., src/main/resources/config.xml)."
//        UrlResource,"For files accessed via URL/URI (e.g., https://example.com/file.jpg or file:/path/to/file)."
//        ByteArrayResource,For data that exists only in memory (like a generated PDF).

//        exists()
//        Returns: boolean
//        Purpose: Checks if the file actually exists at the given path. Always check this before trying to read,
//        or you'll get a FileNotFoundException.

//        isReadable()
//        Returns: boolean
//        Purpose: Checks if the file exists and if your Java application has permission to read it.

//        getFilename()
//        Returns: String
//        Purpose: Gets the name of the file (e.g., homework.pdf). Very useful for setting the Content-Disposition header in your Controller.

//        getFile()
//        Returns: java.io.File
//        Purpose: Returns the standard Java File object.
//        Note: This will fail if the resource is inside a JAR file (use getInputStream() instead).

//        getInputStream()
//        Returns: java.io.InputStream
//        Purpose: This is the most important method. It opens the file so you can stream its bytes to the browser. Spring's ResponseEntity calls this automatically behind the scenes when you put the resource in the .body().

//        contentLength()
//        Returns: long
//        Purpose: Returns the size of the file in bytes. Useful for the Content-Length header so the browser can show a progress bar.
    }

    @PostMapping("/create-assignment")
    public ResponseEntity<CustomResponse> createAssignment(@RequestBody AssignmentDTO assignmentDTO,Authentication authentication){

           Assignment assignment =  assignmentService.createAssignment(assignmentDTO,authentication);
           CustomResponse customResponse = new CustomResponse("Assignment Created Sucesfully",assignmentDTO,HttpStatus.CREATED);
           return ResponseEntity.status(HttpStatus.CREATED).body(customResponse);

    }

    @PostMapping("/approve-assignment")
    public ResponseEntity<CustomResponse> approveAssignment(@RequestParam String assignmentId, Authentication authentication) {

        AssignmentDTO assignmentDTO = assignmentService.approveAssignment(assignmentId, authentication);
        CustomResponse customResponse = new CustomResponse("Assignment Approved", assignmentDTO, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }
}
