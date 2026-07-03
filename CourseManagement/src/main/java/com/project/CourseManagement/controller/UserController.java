package com.project.CourseManagement.controller;

import com.project.CourseManagement.dto.CustomResponse;
import com.project.CourseManagement.dto.UserDTO;
import com.project.CourseManagement.entity.User;
import com.project.CourseManagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping(value = "/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //  Here we used both requestBody (application/json) and want to add multipart which would not work in sprin boot
    //  so to use both we have to structure
    //  the post mapping in this way .
    public ResponseEntity<CustomResponse> registerUser(
            @RequestPart("user") UserDTO userDTO,
            @RequestPart("roles") String roles,
            @RequestPart(value = "profileImage", required = false)
            MultipartFile profileImage) {

        return userService.registerUser(userDTO, roles, profileImage);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponse> loginUser(@RequestBody UserDTO userDTO) {
        UserDTO userDto = userService.loginUser(userDTO);
        CustomResponse customResponse = new CustomResponse("Login Succesful", userDto, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }

    // 2️⃣ Get User by ID
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id, Authentication authentication) {
        logger.info("AUTHENTICATION" + authentication);
        return userService.getUserById(id);
    }
}

