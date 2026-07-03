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
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private Integer maxCapacity;
    private List<UserDTO> enrolledUsers;
    private MentorDTO createdBy;
    private Boolean isUserBooked;
}

