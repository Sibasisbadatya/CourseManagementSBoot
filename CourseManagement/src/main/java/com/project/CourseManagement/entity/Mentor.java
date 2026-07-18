package com.project.CourseManagement.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "mentors")
@NoArgsConstructor
@AllArgsConstructor
@lombok.ToString(exclude = {"createdAssignments", "courses", "user"})
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long mentorId;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    public Boolean isApproved;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    public Boolean isVerified;

    @Min(0)
    @Max(5)
    public Integer rating;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(
            mappedBy = "createdBy",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Assignment> createdAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Course> courses = new ArrayList<>();
}
