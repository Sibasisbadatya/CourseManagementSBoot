package com.project.CourseManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDTO {
    private String assignmentDescription;
    private Boolean docRequired;
    private Boolean mediaRequired;
    private Long courseId;
}
