package com.project.CourseManagement.repository;

import com.project.CourseManagement.entity.Course;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {


    @Modifying
    @Transactional
    @Query(
            " UPDATE Course c " +
                    "SET c.maxCapacity = c.maxCapacity-1" +
                    "WHERE c.id = ?1 AND c.maxCapacity>0"
    )
    int reduceCapacity(Long id);

    Optional<Course> findById(Long id);
}

