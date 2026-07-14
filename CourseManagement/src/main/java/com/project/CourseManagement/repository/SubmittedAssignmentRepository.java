package com.project.CourseManagement.repository;

import com.project.CourseManagement.entity.SubmittedAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmittedAssignmentRepository extends JpaRepository<SubmittedAssignment, Long> {
}
