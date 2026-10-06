package com.project.CourseManagement.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentDTO {
    private Long id;
    private String assignmentDescription;
    private Boolean docRequired;
    private Boolean mediaRequired;
    private Long courseId;
    @Builder.Default
    private Boolean isUserSubmitted=false;
}
