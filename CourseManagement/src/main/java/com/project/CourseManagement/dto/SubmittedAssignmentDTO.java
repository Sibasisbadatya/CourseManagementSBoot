package com.project.CourseManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmittedAssignmentDTO {
    private Long assignmentId;
    private String fileName;
    private String filePath;
    private String audioPath;
    private String audioFileName;
    private String videoPath;
    private String hlsUrl;
    private String mp4Url;
    private String vttUrl;
    private String spriteUrl;
    private String posterUrl;
    private String videoFileName;
    @Builder.Default
    private Boolean isApproved=false;
}
