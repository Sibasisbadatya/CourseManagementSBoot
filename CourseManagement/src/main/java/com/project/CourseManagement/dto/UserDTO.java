package com.project.CourseManagement.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.CourseManagement.enums.UserRole;
import lombok.*;

import java.util.List;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private String name;
    private String password;
    private String email;
    private Long mobileNo;
    private String token;
    private Set<UserRole> roles;
    private String profileImage;

    public UserDTO(String name, String email,String password, Long mobileNo,String profileImage,Set<UserRole> roles) {
        this.name = name;
        this.email = email;
        this.mobileNo = mobileNo;
        this.roles = roles;
        this.password = password;
        this.profileImage=profileImage;
    }
}

