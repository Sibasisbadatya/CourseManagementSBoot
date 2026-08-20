package com.project.CourseManagement.controller;

import com.project.CourseManagement.dto.CourseDTO;
import com.project.CourseManagement.dto.CustomResponse;
import com.project.CourseManagement.entity.Course;
import com.project.CourseManagement.exception.FileStorageException;
import com.project.CourseManagement.response.ApiResponse;
import com.project.CourseManagement.service.CloudinaryService;
import com.project.CourseManagement.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final CloudinaryService cloudinaryService;
    public CourseController(CourseService courseService, CloudinaryService cloudinaryService) {
        this.courseService = courseService;
        this.cloudinaryService = cloudinaryService;
    }


    @GetMapping("/getAllCourse")
    public ResponseEntity<CustomResponse> getAllCourse(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "id") String sortBy) {
        log.info("page: {}, size: {}, sortBy: {}", page, size, sortBy);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Course> courses = courseService.getAllCourse(pageable);
        CustomResponse customResponse = new CustomResponse("All courses fetched Succesfully", courses, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CustomResponse> getCourseByCourseId(@PathVariable Long courseId, Authentication authentication) {
        CourseDTO course = courseService.getCourseByCourseId(courseId, authentication);
        CustomResponse customResponse = new CustomResponse("Course details fetched successfully", course, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }

    @PostMapping("/bookcourse/{courseId}")
    public Course bookCourse(@PathVariable String courseId, Authentication authentication) {
        return courseService.bookCourse(Long.parseLong(courseId), authentication);
    }


    @GetMapping("/getCourses")
    public List<Course> getCoursesByUser(Authentication authentication) {
        return courseService.getCoursesByUser(authentication);
    }

    @PostMapping(value = "/addCourse",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponse> addCourse(@RequestParam("title") String title,
                                                    @RequestParam("description") String description,
                                                    @RequestParam("maxCapacity") Integer maxCapacity,
                                                    @RequestParam("content") String content,
                                                    @RequestParam("courseimage") MultipartFile courseImage, Authentication authentication) throws FileStorageException {
        String imageUrl = "";
        try {
            imageUrl = cloudinaryService.uploadFile(courseImage, "course_images");
        } catch (FileStorageException e) {
            throw new FileStorageException(e.getMessage());
        }
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTitle(title);
        courseDTO.setDescription(description);
        courseDTO.setMaxCapacity(maxCapacity);
        courseDTO.setCourseImage(imageUrl);
        courseDTO.setContent(content);
        log.info("CourseDTO: {}", courseDTO);
        log.info("Content {}",content);
        Course course = courseService.addCourse(courseDTO, authentication);
        CustomResponse customResponse = new CustomResponse("Course succesfully added", course, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(customResponse);
    }


}
