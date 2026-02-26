package com.project.CourseManagement.controller;

import com.project.CourseManagement.dto.CustomResponse;
import com.project.CourseManagement.dto.UserDTO;
import com.project.CourseManagement.entity.User;
import com.project.CourseManagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
//@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1️⃣ Register User
    @PostMapping("/register")
    public ResponseEntity<CustomResponse> registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserDTO userDTO){
        return userService.loginUser(userDTO);
    }

    // 2️⃣ Get User by ID
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id, Authentication authentication) {
        logger.info("AUTHENTICATION"+authentication);
        return userService.getUserById(id);
    }
}

