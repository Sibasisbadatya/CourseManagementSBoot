package com.project.CourseManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorDTO {
    private Integer mentorId;
    private Boolean isApproved;
    private Boolean isVerified;
    private Integer rating;
    private UserDTO user;
}
