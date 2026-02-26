package com.project.CourseManagement.repository;

import com.project.CourseManagement.entity.SavedNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.management.Notification;

public interface NotificationRepository extends JpaRepository<SavedNotification,Long> {
}
