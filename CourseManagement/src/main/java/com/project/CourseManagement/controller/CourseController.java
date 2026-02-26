package com.project.CourseManagement.controller;

import com.project.CourseManagement.dto.CourseDTO;
import com.project.CourseManagement.dto.CustomResponse;
import com.project.CourseManagement.entity.Course;
import com.project.CourseManagement.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/bookcourse/{courseId}")
    public Course bookCourse(@PathVariable String courseId, Authentication authentication) {
        return courseService.bookCourse(Long.parseLong(courseId),authentication);
    }


    @GetMapping("/user/{userId}")
    public List<Course> getCoursesByUser(@PathVariable Long userId) {
        return courseService.getCoursesByUser(userId);
    }

    @PostMapping("/addCourse")
    public ResponseEntity<CustomResponse> addCourse(@RequestBody CourseDTO dto){
        Course course = courseService.addCourse(dto);
        CustomResponse customResponse = new CustomResponse("Course succesfully added",course, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(customResponse);
    }

}
