package com.project.CourseManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime createdAt;


    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "created_by", nullable = false)
    private Mentor createdBy;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "courseId")
    private Course course;

    @OneToMany(
            mappedBy = "assignment",
            cascade = CascadeType.ALL
    )
    private List<SubmittedAssignment> submittedAssignments;

    public void setSubmittedAssignments(SubmittedAssignment submittedAssignments) {
        this.submittedAssignments.add(submittedAssignments);
    }
}
