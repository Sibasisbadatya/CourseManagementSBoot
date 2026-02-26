package com.project.CourseManagement.service;

import com.project.CourseManagement.dto.AssignmentDTO;
import com.project.CourseManagement.dto.CustomResponse;
import com.project.CourseManagement.entity.Assignment;
import com.project.CourseManagement.entity.User;
import com.project.CourseManagement.exception.*;
import com.project.CourseManagement.repository.AssignmentRepository;
import com.project.CourseManagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;


@Service
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;
    private static final String BASE_PATH = "uploads/assignments";
    private final SimpMessagingTemplate messagingTemplate;
    private static final Logger logger = LoggerFactory.getLogger(AssignmentService.class);

    public AssignmentService(AssignmentRepository assignmentRepository, UserRepository userRepository, ModelMapper modelMapper, NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    public AssignmentDTO uploadAssignment(Long courseId, MultipartFile file, MultipartFile audio, String email) throws FileStorageException {
        if (file.isEmpty()) {
            throw new FileNotFound("Assignment Documnent is Empty");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotPresent("User Not Found to Upload Assignment"));

        try {
            String userFolder = BASE_PATH + email;
            Files.createDirectories(Paths.get(userFolder));

            String filePath = userFolder + "/" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

            String audioPath = null;
            if (audio != null && !audio.isEmpty()) {
                audioPath = userFolder + "/" + audio.getOriginalFilename();
                Files.copy(audio.getInputStream(), Paths.get(audioPath), StandardCopyOption.REPLACE_EXISTING);
            }

            Assignment assignment = new Assignment();
            assignment.setCourseId(courseId);
            assignment.setUser(user);
            assignment.setFilePath(filePath);
            assignment.setFileName(file.getOriginalFilename());
            if (audioPath != null) {
                assignment.setAudioPath(audioPath);
                assignment.setAudioFileName(audio.getOriginalFilename());
            }
            assignment.setUploadedAt(LocalDateTime.now());
            assignmentRepository.save(assignment);
            return modelMapper.map(assignment, AssignmentDTO.class);
        } catch (IOException e) {
            throw new FileStorageException("Couldn't store file on disk");
        } catch (Exception e) {
            throw new InternalServerError("Error Occured in Uploading");
        }
    }

    public ResponseEntity<CustomResponse> getAssignment(Long assignmentId, String type) throws IOException {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new AssignmentNotFound("Assignment Not Found"));
        boolean isFile = type == "audio" ? true : false;
        Path path = Paths.get(isFile ? assignment.getFilePath() : assignment.getAudioPath());
        String fileName = isFile ? assignment.getFileName() : assignment.getAudioFileName();
        Resource resource = new UrlResource(path.toUri());
        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(fileName, StandardCharsets.UTF_8)
                .build();

        CustomResponse customResponse = new CustomResponse("Downloadable Data fetched Succesfully", assignment, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .body(customResponse);
    }

    @Transactional
    public AssignmentDTO approveAssignment(String assignmentId, Authentication authentication) {
        Long id = Long.parseLong(assignmentId);
        Assignment assignment = assignmentRepository.findById(id).orElseThrow(() -> new AssignmentNotFound("Assignment Not Found to Approve"));
        assignment.setApproved(true);
        try {
            assignmentRepository.save(assignment);
            try {
                notificationService.saveAndSendNotificationToPersonal("Assignment Approved", assignment.getUser(), "/queue/assignment-updates", assignment);
            } catch (RuntimeException e) {
                logger.info("Error in Send Msg", e.getMessage());
                throw new RuntimeException(e);
            }
            AssignmentDTO assignmentDTO = modelMapper.map(assignment, AssignmentDTO.class);
            return assignmentDTO;
        } catch (RuntimeException e) {
            logger.info(e.getMessage());
            throw new InternalServerError("Unable to Approve Request");
        }

    }

}
