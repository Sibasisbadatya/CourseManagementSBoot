package com.project.CourseManagement.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private Integer maxCapacity;
    private String courseImage;
    private String content;
    private List<UserDTO> enrolledUsers;
    private MentorDTO createdBy;
    @Builder.Default
    private Boolean isUserBooked=false;
}

