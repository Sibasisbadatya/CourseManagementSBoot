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


    @GetMapping("/getAllCourse")
    public ResponseEntity<CustomResponse> getAllCourse(){
        List<Course> courses = courseService.getAllCourse();
        CustomResponse customResponse = new CustomResponse("All courses fetched Succesfully",courses, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CustomResponse> getCourseByCourseId(@PathVariable Long courseId,Authentication authentication){
        CourseDTO course = courseService.getCourseByCourseId(courseId,authentication);
        CustomResponse customResponse = new CustomResponse("Course details fetched successfully",course, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }

    @PostMapping("/bookcourse/{courseId}")
    public Course bookCourse(@PathVariable String courseId, Authentication authentication) {
        return courseService.bookCourse(Long.parseLong(courseId),authentication);
    }


    @GetMapping("/getCourses")
    public List<Course> getCoursesByUser(Authentication authentication) {
        return courseService.getCoursesByUser(authentication);
    }

    @PostMapping("/addCourse")
    public ResponseEntity<CustomResponse> addCourse(@RequestBody CourseDTO dto,Authentication authentication){
        Course course = courseService.addCourse(dto,authentication);
        CustomResponse customResponse = new CustomResponse("Course succesfully added",course, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(customResponse);
    }


}
