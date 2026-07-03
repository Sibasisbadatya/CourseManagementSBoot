package com.project.CourseManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "assignment")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String assignmentDescription;
    private String fileName;
    private String filePath;
    private String audioPath;
    private String audioFileName;
    private String videoPath;
    private String videoFileName;
    private Boolean isApproved;
    private LocalDateTime uploadedAt;
    private LocalDateTime createdAt;


    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "created_by", nullable = false)
    private Mentor createdBy;

    @ManyToOne
    @JoinColumn(name = "submitted_by")
    private User submittedBy;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "courseId")
    private Course course;


}
