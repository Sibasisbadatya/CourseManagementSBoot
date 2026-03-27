package com.project.CourseManagement.service;

import com.project.CourseManagement.dto.CourseDTO;
import com.project.CourseManagement.entity.Course;
import com.project.CourseManagement.entity.User;
import com.project.CourseManagement.exception.DataNotFound;
import com.project.CourseManagement.exception.InternalServerError;
import com.project.CourseManagement.exception.OutOfCapacity;
import com.project.CourseManagement.repository.CourseRepository;
import com.project.CourseManagement.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepo;
    private final UserRepository userRepo;
    private final ModelMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    public CourseService(CourseRepository courseRepo, UserRepository userRepo, ModelMapper mapper) {
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
        this.mapper = mapper;
    }

    public Course bookCourse(Long courseId, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        User user = (User) authentication.getPrincipal();
        int updatedCount = courseRepo.reduceCapacity(courseId);
        if (updatedCount <= 0) {
            throw new OutOfCapacity("All Slots  has been booked");
        }
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new DataNotFound("can't found the course to book"));
//        course.setMaxCapacity(course.getMaxCapacity() - 1);
        course.setUser(user);
        return courseRepo.save(course);
    }

    public Course addCourse(CourseDTO dto) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setMaxCapacity(dto.getMaxCapacity());
        Optional<User> user = userRepo.findById(dto.getUserId());
        user.ifPresent(course::setUser);
        logger.info("COURSE {}", course);
        try {
            return courseRepo.save(course);
        } catch (RuntimeException e) {
            logger.info("Error {}", e.getMessage());
            throw new InternalServerError("Couldn't able to add course");
        }

    }

    public List<Course> getCoursesByUser(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        return courseRepo.findByUserId(user.getId());
    }
}
