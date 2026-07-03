package com.project.CourseManagement.repository;

import com.project.CourseManagement.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorRepository extends JpaRepository<Mentor,Integer> {
}
