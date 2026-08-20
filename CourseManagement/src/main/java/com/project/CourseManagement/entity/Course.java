package com.project.CourseManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@lombok.ToString(exclude = {"createdBy", "assignmentList", "enrolledUsers"})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Integer maxCapacity;
    private String courseImage;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "createdBy")
    @JsonBackReference
    private Mentor createdBy;

    @JsonIgnore
    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonBackReference
    private List<Assignment> assignmentList = new ArrayList<>();

    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> enrolledUsers = new ArrayList<>();

    public void setEnrolledUsers(User enrolledUser) {
        this.enrolledUsers.add(enrolledUser);
    }
}

