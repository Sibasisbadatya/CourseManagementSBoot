package com.project.CourseManagement.service;

import com.project.CourseManagement.dto.AssignmentDTO;
import com.project.CourseManagement.dto.CourseDTO;
import com.project.CourseManagement.dto.MentorDTO;
import com.project.CourseManagement.entity.Assignment;
import com.project.CourseManagement.entity.Course;
import com.project.CourseManagement.entity.User;
import com.project.CourseManagement.exception.AuthenticationError;
import com.project.CourseManagement.exception.DataNotFound;
import com.project.CourseManagement.exception.InternalServerError;
import com.project.CourseManagement.exception.OutOfCapacity;
import com.project.CourseManagement.repository.CourseRepository;
import com.project.CourseManagement.repository.MentorRepository;
import com.project.CourseManagement.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class CourseService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CourseRepository courseRepo;
    private final UserRepository userRepo;
    private final ModelMapper mapper;
    private final MentorRepository mentorRepository;

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private User getUserDetailsFromAuthentication(Authentication authentication) {
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

    public CourseDTO getCourseByCourseId(Long courseId, Authentication authentication) {
        User user = userRepo.findByEmail(authentication.getName()).orElseThrow(() -> new AuthenticationError("Authenticated User Not Found"));
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
        courseDTO.setCourseImage(course.getCourseImage());
        courseDTO.setContent(course.getContent());
        courseDTO.setIsUserBooked(false);
        if (course.getEnrolledUsers() != null && course.getEnrolledUsers().contains(user)) {
            courseDTO.setIsUserBooked(true);
        }

        List<Assignment> assignmentList = course.getAssignmentList();
        Set<Long> submittedAssignmentsId = user.getSubmittedAssignments()
                .stream()
                        .map(submittedAssignment -> submittedAssignment.getAssignment().getId())
                        .collect(Collectors.toSet());

        courseDTO.setAssignmentLists(
                assignmentList.stream().map(assignment -> {
                    AssignmentDTO assignmentDTO = new AssignmentDTO();
                    assignmentDTO.setCourseId(assignment.getCourse().getId());
                    assignmentDTO.setId(assignment.getId());
                    assignmentDTO.setAssignmentDescription(assignment.getAssignmentDescription());
                    assignmentDTO.setMediaRequired(assignment.getMediaRequired());
                    assignmentDTO.setDocRequired(assignment.getDocRequired());
                    assignmentDTO.setIsUserSubmitted(submittedAssignmentsId.contains(assignment.getId()));
                    return assignmentDTO;
                }).toList()
        );

//       return courseRepo.findById(courseId).orElseThrow(()-> new DataNotFound("Course Details by this id not found"));
        return courseDTO;
    }

    @Transactional
    public Course bookCourse(Long courseId, Authentication authentication) {
        User user = userRepo.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new DataNotFound("User not found with email: " + authentication.getName()));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() ->
                        new DataNotFound("can't found the course to book"));
        int updatedCount = courseRepo.reduceCapacity(courseId);
        if (updatedCount <= 0) {
            throw new OutOfCapacity("All Slots have been booked");
        }
        entityManager.refresh(course);
        course.setEnrolledUsers(user);
        return courseRepo.save(course);
    }

    public Course addCourse(CourseDTO dto, Authentication authentication) {
        User mentorUser = getUserDetailsFromAuthentication(authentication);
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setMaxCapacity(dto.getMaxCapacity());
        course.setCreatedBy(mentorUser.getMentor());
        course.setCourseImage(dto.getCourseImage());
        course.setContent(dto.getContent());
        try {
            return courseRepo.save(course);
        } catch (RuntimeException e) {
            logger.info("Error {}", e.getMessage());
            throw new InternalServerError("Couldn't able to add course");
        }

    }

    @Transactional()
    public List<Course> getCoursesByUser(Authentication authentication) {
        String emailId = authentication.getName();
        User user = userRepo.findByEmail(emailId).orElseThrow(() -> new DataNotFound("User not found with email: " + emailId));

        List<Course> courses = new ArrayList<>();
        try {
            courses = user.getCourses();
        } catch (RuntimeException e) {
            logger.info("Error in fetching courses {}", e.getMessage());
            throw new InternalServerError("Couldn't able to fetch courses for user");
        }
        courses.stream()
                .map(course -> mapper.map(course, CourseDTO.class))
                .forEach(courseDTO -> {
                    courseDTO.setIsUserBooked(true);
                });
        return courses;
    }

    public Page<Course> getAllCourse(Pageable pageable) {
        Page<Course> courses = courseRepo.findAll(pageable);
        courses.stream()
                .forEach(course -> course.setContent(null));
        return courses;
    }
}
