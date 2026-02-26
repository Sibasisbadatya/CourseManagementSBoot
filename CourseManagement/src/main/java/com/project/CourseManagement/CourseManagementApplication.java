package com.project.CourseManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.project.CourseManagement.repository")
@EntityScan("com.project.CourseManagement.entity")
public class CourseManagementApplication {

	public static void main(String[] args) {
        SpringApplication.run(CourseManagementApplication.class, args);
	}

}
