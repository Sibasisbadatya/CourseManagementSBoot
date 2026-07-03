package com.project.CourseManagement.repository;

import com.project.CourseManagement.entity.Role;
import com.project.CourseManagement.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {
   Optional<Role> findByName(UserRole name);
}
