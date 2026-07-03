package com.project.CourseManagement.service;

import com.project.CourseManagement.dto.CourseDTO;
import com.project.CourseManagement.dto.MentorDTO;
import com.project.CourseManagement.entity.Course;
import com.project.CourseManagement.entity.Mentor;
import com.project.CourseManagement.entity.User;
import com.project.CourseManagement.exception.DataNotFound;
import com.project.CourseManagement.exception.InternalServerError;
import com.project.CourseManagement.exception.OutOfCapacity;
import com.project.CourseManagement.repository.CourseRepository;
import com.project.CourseManagement.repository.MentorRepository;
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
    private final MentorRepository mentorRepository;

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private User getUserDetailsFromAuthentication(Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        return (User) authentication.getPrincipal();
    }

    public CourseService(CourseRepository courseRepo, UserRepository userRepo, ModelMapper mapper, MentorRepository mentorRepository) {
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
        this.mapper = mapper;
        this.mentorRepository = mentorRepository;
    }

    public CourseDTO getCourseByCourseId(Long courseId,Authentication authentication) {
        User user = getUserDetailsFromAuthentication(authentication);
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new DataNotFound("Course Details by this id not found"));
        MentorDTO mentorDTO = null;
        if (course.getCreatedBy() != null) {
            mentorDTO = mapper.map(course.getCreatedBy(), MentorDTO.class);
        }
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTitle(course.getTitle());
        courseDTO.setMaxCapacity(course.getMaxCapacity());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setCreatedBy(mentorDTO);
        courseDTO.setIsUserBooked(false);
        if (course.getCreatedBy() != null && course.getCreatedBy().getUser().getId().equals(user.getId())) {
           courseDTO.setIsUserBooked(true);
        }
//       return courseRepo.findById(courseId).orElseThrow(()-> new DataNotFound("Course Details by this id not found"));
        return courseDTO;
    }

    public Course bookCourse(Long courseId, Authentication authentication) {
        User user = getUserDetailsFromAuthentication(authentication);
        int updatedCount = courseRepo.reduceCapacity(courseId);
        if (updatedCount <= 0) {
            throw new OutOfCapacity("All Slots  has been booked");
        }
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new DataNotFound("can't found the course to book"));
//        course.setMaxCapacity(course.getMaxCapacity() - 1);
        course.setEnrolledUsers(user);
        return courseRepo.save(course);
    }

    public Course addCourse(CourseDTO dto,Authentication authentication) {
        User mentorUser = getUserDetailsFromAuthentication(authentication);
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setMaxCapacity(dto.getMaxCapacity());
        course.setCreatedBy(mentorUser.getMentor());
        try {
            return courseRepo.save(course);
        } catch (RuntimeException e) {
            logger.info("Error {}", e.getMessage());
            throw new InternalServerError("Couldn't able to add course");
        }

    }

    public List<Course> getCoursesByUser(Authentication authentication) {
        User user = getUserDetailsFromAuthentication(authentication);
        List<Course> courses = user.getCourses();
        courses.stream()
                .map(course -> mapper.map(course, CourseDTO.class))
                .forEach(courseDTO -> {
                    courseDTO.setIsUserBooked(true);
                });
        return courses;
    }

    public List<Course> getAllCourse(){
        return courseRepo.findAll();
    }
}
