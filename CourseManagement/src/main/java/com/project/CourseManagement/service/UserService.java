package com.project.CourseManagement.service;

import com.project.CourseManagement.dto.CustomResponse;
import com.project.CourseManagement.dto.UserDTO;
import com.project.CourseManagement.entity.Mentor;
import com.project.CourseManagement.entity.Role;
import com.project.CourseManagement.entity.User;
import com.project.CourseManagement.enums.UserRole;
import com.project.CourseManagement.events.RegistrationEvent;
import com.project.CourseManagement.exception.*;
import com.project.CourseManagement.repository.RolesRepository;
import com.project.CourseManagement.repository.UserRepository;
import com.project.CourseManagement.utils.JWTUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    private final ApplicationEventPublisher publisher;
    private final RolesRepository rolesRepository;
    private final FileUploadService fileUploadService;

    public UserService(UserRepository userRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTUtils jwtUtils, JavaMailSender javaMailSender, ApplicationEventPublisher publisher, RolesRepository rolesRepository, FileUploadService fileUploadService) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.javaMailSender = javaMailSender;
        this.publisher = publisher;
        this.rolesRepository = rolesRepository;
        this.fileUploadService = fileUploadService;
    }


    public ResponseEntity<CustomResponse> registerUser(UserDTO dto, String roles, MultipartFile profileImage) {
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
            Set<UserRole> userRoles = new HashSet<>();
            //setting roles to the user
            if (roles != null && !roles.isEmpty()) {
                userRoles = Arrays.stream(roles.split(","))
                        .map(String::trim)
                        .map(UserRole::valueOf)
                        .collect(Collectors.toSet());
                for (UserRole userRole : userRoles) {
                    Role role = rolesRepository.findByName(userRole).orElseThrow(() -> new DataNotFound("Error: Specified Role '" + userRole + "' not found in DB initialization script."));
                    user.addRole(role);
                    if (userRole == UserRole.MENTOR) {
                        Mentor mentor = new Mentor();
                        mentor.setUser(user);
                        user.setMentor(mentor);
                    }
                }
            } else {
                // Default fallback assignment rule if no explicit role is submitted
                Role defaultRole = rolesRepository.findByName(UserRole.USER) // or whatever your student role enum is
                        .orElseThrow(() -> new RuntimeException("Error: Default Student Role not initialized."));
                user.addRole(defaultRole);
            }
            try {
                if ((profileImage.getOriginalFilename() != null) & !profileImage.getOriginalFilename().isEmpty()) {
                    user.setProfileImage(profileImage.getOriginalFilename());
                    fileUploadService.uploadUserProfileImage(profileImage, dto.getEmail());
                }
            } catch (IOException e) {
                logger.info(e.getMessage());
                throw new FileStorageException("Unable to Upload Profile Image");
            }
            User registeredUser = userRepo.save(user);
            UserDTO userDTO = modelMapper.map(registeredUser, UserDTO.class);
            userDTO.setRoles(userRoles);
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(dto.getEmail());
                message.setSubject("Registraton Succesful");
                message.setText("Registration to Course Managemnt is Succeful");
                publisher.publishEvent(new RegistrationEvent(userDTO));
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
            CustomResponse customResponse = new CustomResponse("User Registered Succesfully", userDTO, HttpStatus.CREATED);
            return ResponseEntity.status(HttpStatus.CREATED).body(customResponse);
        } catch (RuntimeException | FileStorageException e) {
            logger.info(e.getMessage());
            throw new UserRegistrationError("Error in Registering user");
        }
    }

    public UserDTO loginUser(UserDTO dto) {
        System.out.println("LOGGING IN");
        User logginguser = userRepo.findByEmail(dto.getEmail()).orElseThrow(()-> new UserNotPresent("User Not Registered"));
        logger.info("User fetched {}",logginguser);
        List<Role> userRoles = logginguser.getRoles();
        Set<UserRole> roles = userRoles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        UserDTO userDTO = modelMapper.map(logginguser, UserDTO.class);
        userDTO.setRoles(roles);
        logger.info("User DTO {}",userDTO);
        String token = null;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
            token = jwtUtils.generateToken(dto.getEmail()); //Here email used as UserName
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        userDTO.setToken(token);
        return userDTO;

    }

    public UserDTO getUserById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }
}

