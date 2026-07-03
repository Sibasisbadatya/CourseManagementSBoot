package com.project.CourseManagement.events;

import com.project.CourseManagement.dto.UserDTO;
import lombok.Data;

@Data
public class RegistrationEvent {
    private UserDTO userDTO;

    public RegistrationEvent(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

}
