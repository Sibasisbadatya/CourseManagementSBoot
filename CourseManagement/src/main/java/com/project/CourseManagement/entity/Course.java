package com.project.CourseManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Integer maxCapacity;

    @ManyToOne
    @JoinColumn(name = "createdBy")
    @JsonBackReference
    private Mentor createdBy;

    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonBackReference
    private List<Assignment> assignmentList = new ArrayList<>();

    @ManyToMany(mappedBy = "courses")
    @JsonManagedReference
    private List<User> enrolledUsers = new ArrayList<>();

    public void setEnrolledUsers(User enrolledUser) {
        this.enrolledUsers.add(enrolledUser);
    }
}

