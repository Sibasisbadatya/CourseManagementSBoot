package com.project.CourseManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmittedAssignmentDTO {
    private String fileName;
    private String filePath;
    private String audioPath;
    private String audioFileName;
    private String videoPath;
    private String videoFileName;
    private Boolean isApproved;
}
