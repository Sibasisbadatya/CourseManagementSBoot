package com.project.CourseManagement.service;

import com.project.CourseManagement.dto.CustomResponse;
import com.project.CourseManagement.dto.UserDTO;
import com.project.CourseManagement.entity.User;
import com.project.CourseManagement.exception.UserAlreadyExists;
import com.project.CourseManagement.exception.UserNotPresent;
import com.project.CourseManagement.exception.UserRegistrationError;
import com.project.CourseManagement.repository.UserRepository;
import com.project.CourseManagement.utils.JWTUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final JavaMailSender javaMailSender;

    public UserService(UserRepository userRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTUtils jwtUtils, JavaMailSender javaMailSender) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.javaMailSender = javaMailSender;
    }


    public ResponseEntity<CustomResponse> registerUser(UserDTO dto) {
        Optional<User> isUserAlreadyPresent = userRepo.findByEmail(dto.getEmail());
        isUserAlreadyPresent.ifPresent(user -> {
            throw new UserAlreadyExists("User Already Exists");
        });

        try {
            User user = new User();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setMobileNo(dto.getMobileNo());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            User registeredUser = userRepo.save(user);
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(dto.getEmail());
                message.setSubject("Registraton Succesful");
                message.setText("Registration to Course Managemnt is Succeful");
                javaMailSender.send(message);
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
            CustomResponse customResponse = new CustomResponse("User Registered Succesfully",registeredUser, HttpStatus.CREATED);
            return ResponseEntity.status(HttpStatus.CREATED).body(customResponse);
        } catch (RuntimeException e) {
            logger.info(e.getMessage());
            throw new UserRegistrationError("Error in Registering user");
        }
    }

    public String loginUser(UserDTO dto) {
        System.out.println("LOGGING INNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
        Optional<User> logginguser = userRepo.findByEmail(dto.getEmail());
        logginguser.orElseThrow(() -> new UserNotPresent("User Not Registered"));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
            return jwtUtils.generateToken(dto.getEmail()); //Here email used as UserName
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public UserDTO getUserById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }
}

