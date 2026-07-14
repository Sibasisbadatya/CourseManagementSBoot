package com.project.CourseManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmittedAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;
    private String audioPath;
    private String audioFileName;
    private String videoPath;
    private String videoFileName;
    private Boolean isApproved;
    private LocalDateTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "submitted_by")
    private User submittedBy;

    @ManyToOne
    @JoinColumn(name = "assignment")
    private Assignment assignment;
}
