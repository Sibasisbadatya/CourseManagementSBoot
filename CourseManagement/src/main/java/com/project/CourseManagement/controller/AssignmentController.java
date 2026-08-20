package com.project.CourseManagement.controller;

import com.project.CourseManagement.dto.AssignmentDTO;
import com.project.CourseManagement.dto.SubmittedAssignmentDTO;
import com.project.CourseManagement.dto.CustomResponse;
import com.project.CourseManagement.dto.FileItemDTO;
import com.project.CourseManagement.entity.User;
import com.project.CourseManagement.enums.FileType;
import com.project.CourseManagement.service.AssignmentService;
import com.project.CourseManagement.service.StreamingService;
import jakarta.transaction.Transactional;
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
    private final StreamingService streamingService;

    private static final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    public AssignmentController(AssignmentService assignmentService, StreamingService streamingService) {
        this.assignmentService = assignmentService;
        this.streamingService = streamingService;
    }


    @GetMapping("/stream/{assignmentId}/{type}")
    public ResponseEntity<Resource> stream(
            @PathVariable String assignmentId,
            @PathVariable FileType type,
            @RequestHeader(value = "Range", required = false) String rangeHeader
//            It reads the Range header sent by the browser eg Range: bytes=0-1023

    ) throws IOException {
        return streamingService.streamFile(Long.parseLong(assignmentId), type, rangeHeader);
    }


    @GetMapping("/getAssignmentData")
    public ResponseEntity<CustomResponse> getAssignmentData(@RequestParam Long assignmentId, @RequestParam FileType type) {
        Resource resource = assignmentService.loadFileAsResource(assignmentId, type);
        return null;
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

    @GetMapping("/getAssignementsByCourseId")
    public ResponseEntity<CustomResponse> getAllAssignments(@RequestBody AssignmentDTO assignmentDTO,Authentication authentication){
        Long courseId = assignmentDTO.getCourseId();
        List<AssignmentDTO> assignmentList = assignmentService.getAllAssignment(courseId,authentication);
        CustomResponse customResponse = new CustomResponse("Assignments fetched Succesfully", assignmentList, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }

    @PostMapping("/create-assignment")
    public ResponseEntity<CustomResponse> createAssignment(@RequestBody AssignmentDTO assignmentDTO, Authentication authentication) {

        AssignmentDTO assignment = assignmentService.createAssignment(assignmentDTO, authentication);
        CustomResponse customResponse = new CustomResponse("Assignment Created Sucesfully", assignment, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(customResponse);

    }



    @PostMapping("/submit-assignment")
    @Transactional
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
        SubmittedAssignmentDTO assignmentDTO = assignmentService.submitAssignment(assignmentId, file, audio, video, authentication.getName());
        CustomResponse customResponse = new CustomResponse("Assignment uploaded successfully", assignmentDTO, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }

    @GetMapping("/get-submitted-assignment/{assignmentId}")
    public ResponseEntity<CustomResponse> getSubmittedAssignment(
            @PathVariable Long assignmentId,
            Authentication authentication
    ) {
        List<FileItemDTO> assignmentFiles = new ArrayList<>();
        assignmentFiles = assignmentService.getAssignmentByAssignmentId(assignmentId);
        logger.info("Assignments{}", assignmentFiles);
        CustomResponse customResponse = new CustomResponse("Assignments fetched Succesfully", assignmentFiles, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);

    }

    @GetMapping("get-submitted-assignments-by-assignment/{assignmentId}")
    public ResponseEntity<CustomResponse> getSubmittedAssignments(
            @PathVariable Long assignmentId,
            Authentication authentication
    ){
       List<SubmittedAssignmentDTO> submittedAssignmentsList = assignmentService.getSubmittedAssignments(assignmentId, authentication);
        CustomResponse customResponse = new CustomResponse("Submitted Assignments fetched Succesfully", submittedAssignmentsList, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }

    @PostMapping("/approve-assignment")
    public ResponseEntity<CustomResponse> approveAssignment(@RequestParam String assignmentId, Authentication authentication) {

        SubmittedAssignmentDTO assignmentDTO = assignmentService.approveAssignment(assignmentId, authentication);
        CustomResponse customResponse = new CustomResponse("Assignment Approved", assignmentDTO, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }
}
