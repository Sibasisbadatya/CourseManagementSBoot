package com.project.CourseManagement.service;

import com.project.CourseManagement.dto.*;
import com.project.CourseManagement.entity.*;
import com.project.CourseManagement.enums.FileType;
import com.project.CourseManagement.exception.*;
import com.project.CourseManagement.repository.AssignmentRepository;
import com.project.CourseManagement.repository.CourseRepository;
import com.project.CourseManagement.repository.SubmittedAssignmentRepository;
import com.project.CourseManagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private static final Logger logger = LoggerFactory.getLogger(AssignmentService.class);
    private final SubmittedAssignmentRepository submittedAssignmentRepository;
    private final CourseService courseService;
    private final CloudinaryService cloudinaryService;

    public AssignmentService(AssignmentRepository assignmentRepository, UserRepository userRepository, CourseRepository courseRepository, ModelMapper modelMapper, NotificationService notificationService, SimpMessagingTemplate messagingTemplate, SubmittedAssignmentRepository submittedAssignmentRepository, CourseService courseService, CloudinaryService cloudinaryService) {
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
        this.submittedAssignmentRepository = submittedAssignmentRepository;
        this.courseService = courseService;
        this.cloudinaryService = cloudinaryService;
    }


    @Transactional
    public SubmittedAssignmentDTO submitAssignment(String forLocalStorage, Long assignmentId, MultipartFile file, MultipartFile audio, MultipartFile video, String email) throws FileStorageException {
        if (file.isEmpty()) {
            throw new FileNotFound("Assignment Document is Empty");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotPresent("User Not Found to Upload Assignment"));

        try {
            String userFolder = "user/uploads/" + email;
            Files.createDirectories(Paths.get(userFolder));

            String filePath = userFolder + "/" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
//            StandardCopyOption is an enum in Java (NIO package) used while copying or moving files.
//                    | Option             | Meaning                                |
//                    | ------------------ | -------------------------------------- |
//                    | `REPLACE_EXISTING` | Replace file if it already exists without this java will throw FileAlreadyExistsException      |
//                    | `COPY_ATTRIBUTES`  | Copy file metadata (like timestamps)  Not only file content is copied, but also its metadata. like creation time,lastmodified,last accessed ,permissions |
//                    | `ATOMIC_MOVE`      | Move file as a single atomic operation No partial move. |


            Path audioPath = null;
            Path videoPath = null;
//                    1. Path (The "Address")
//                    Path is an interface. Think of it as the GPS coordinates for a file. It doesn't mean the file exists; it’s just the representation of the location.
//
//                    Key Detail: It replaced java.io.File.
//                    What it holds: The directory structure, the filename, and the file extension.

//                    2. Paths (The "GPS Device")
//                    Paths is a utility class used to create Path objects. You use it to turn a String into a usable Java object.
            // Creating a path from a String
//                    Path myFile = Paths.get("C:/uploads/assignments/math.pdf");

            // You can also pass parts separately (it handles the slashes for you!)
//                    Path sameFile = Paths.get("C:", "uploads", "assignments", "math.pdf");


//                    3. Files (The "Worker")
//                    If Path is the address, Files is the person who actually goes to that address to do work.
//                    It is a utility class full of static methods to manipulate the files.

//                    Method,What it does,Example
//                    Files.exists(path),Checks if the file is there.,boolean isThere = Files.exists(myPath);
//                    "Files.copy(src, dest)",Copies a file.,"Files.copy(oldPath, newPath);"
//                    "Files.move(src, dest)",Renames or moves a file.,"Files.move(oldPath, newPath);"
//                    Files.delete(path),Deletes a file.,Files.delete(myPath);
//                    Files.size(path),Gets the size in bytes.,long bytes = Files.size(myPath);
//                    Files.probeContentType(path),Guesses the file type (MIME).,String type = Files.probeContentType(myPath);

            if (audio != null && !audio.isEmpty()) {
                audioPath = Paths.get(userFolder, audio.getOriginalFilename());
                Files.copy(audio.getInputStream(), audioPath, StandardCopyOption.REPLACE_EXISTING);
            }
            if (video != null && !video.isEmpty()) {
                videoPath = Paths.get(userFolder, video.getOriginalFilename());
                Files.copy(video.getInputStream(), videoPath, StandardCopyOption.REPLACE_EXISTING);
            }
            log.info("AssignmentId", assignmentId);
            Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new DataNotFound("Assignment Not Found to Submit"));
            SubmittedAssignment assignmentSubmission = new SubmittedAssignment();
            assignmentSubmission.setSubmittedBy(user);
            assignmentSubmission.setFilePath(filePath);
            assignmentSubmission.setFileName(file.getOriginalFilename());

            if (audioPath != null) {
                assignmentSubmission.setAudioPath(audioPath.toString());
                assignmentSubmission.setAudioFileName(audio.getOriginalFilename());
            }
            if (videoPath != null) {
                assignmentSubmission.setVideoPath(videoPath.toString());
                assignmentSubmission.setVideoFileName(video.getOriginalFilename());
            }
            assignmentSubmission.setUploadedAt(LocalDateTime.now());
//            before setting usbmitted asignment we have to make link parent to child
            assignmentSubmission.setAssignment(assignment);
//          setting SubmittedAssignmnets which will be linked to this assignments
            assignment.setSubmittedAssignments(assignmentSubmission);

            assignmentRepository.save(assignment);
            return modelMapper.map(assignment, SubmittedAssignmentDTO.class);
        } catch (IOException e) {
            throw new FileStorageException("Couldn't store file on disk");
        } catch (Exception e) {
            log.info("Error Occured in Uploading {}", e.getMessage());
            throw new InternalServerError("Error Occurred in Uploading");
        }
    }

    @Transactional
    public SubmittedAssignmentDTO submitAssignment(Long assignmentId, MultipartFile file, MultipartFile audio, MultipartFile video, String email) throws FileStorageException {
        if (file.isEmpty()) {
            throw new FileNotFound("Assignment Document is Empty");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotPresent("User Not Found to Upload Assignment"));

        try {
            String userFolder = email + "/assignments";
            System.out.println("SIbasis Badatya 12345");
            String filePath = null;
            String audioPath = null;
            String videoPath = null;
            String vttUrl = null;
            if (file != null && !file.isEmpty()) {
                filePath = cloudinaryService.uploadFile(file, userFolder);
            }
            if (audio != null && !audio.isEmpty()) {
                audioPath = cloudinaryService.uploadFile(audio, userFolder);
            }
            if (video != null && !video.isEmpty()) {
                CloudinaryVideoResponse videoResponse = cloudinaryService.uploadVideo(video, userFolder);
                log.info("Video Response: {}", videoResponse);

                videoPath = videoResponse.getSecureUrl();
                vttUrl = videoResponse.getVttUrl();

                log.info("Playable Video Path: {}", videoPath);
                log.info("HLS Path: {}", videoResponse.getHlsUrl());
                log.info("VTT Path: {}", vttUrl);
            }
            log.info("AssignmentId", assignmentId);
            Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new DataNotFound("Assignment Not Found to Submit"));
            SubmittedAssignment assignmentSubmission = new SubmittedAssignment();
            assignmentSubmission.setSubmittedBy(user);
            assignmentSubmission.setFilePath(filePath);
            assignmentSubmission.setFileName(file.getOriginalFilename());

            if (audioPath != null) {
                assignmentSubmission.setAudioPath(audioPath.toString());
                assignmentSubmission.setAudioFileName(audio.getOriginalFilename());
            }
            if (videoPath != null) {
                assignmentSubmission.setVideoPath(videoPath.toString());
                assignmentSubmission.setVttUrl(vttUrl.toString());
                assignmentSubmission.setVideoFileName(video.getOriginalFilename());
            }
            assignmentSubmission.setUploadedAt(LocalDateTime.now());
            assignmentSubmission.setAssignment(assignment);
            assignment.setSubmittedAssignments(assignmentSubmission);
            try {
                assignmentRepository.save(assignment);
            } catch (RuntimeException e) {
                throw new InternalServerError("Error Occurred in Uploading Assignment");
            }
            return toSubmittedAssignmentDto(assignmentSubmission);
        } catch (IOException e) {
            log.info("Error Ocuured in uploading assignment {} ", e.getMessage());
            e.printStackTrace();
            throw new FileStorageException("Couldn't store file on disk");
        } catch (Exception e) {
            log.info("Error Occured in Uploading {}", e.getMessage());
            throw new InternalServerError("Error Occurred in Uploading");
        }
    }


    public AssignmentDTO createAssignment(AssignmentDTO assignmentDTO, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Assignment savedAssignment = null;
        Long courseId = assignmentDTO.getCourseId();
        String assignmentDescription = assignmentDTO.getAssignmentDescription();
        Course course = null;
        try {
            course = courseRepository.findById(courseId).orElseThrow(() -> new DataNotFound("Course Not Found to create Assignment"));
        } catch (Exception e) {
            throw new DataNotFound("Course Not Found to create Assignment");
        }
        Assignment assignment = new Assignment();
        Mentor mentor = user.getMentor();
        if (mentor == null) {
            throw new UserNotPresent("Don't seem to be a mentor to create assignment");
        }
        try {
            assignment.setCourse(course);
            assignment.setCreatedBy(mentor);
            assignment.setAssignmentDescription(assignmentDescription);
            savedAssignment = assignmentRepository.save(assignment);
            return modelMapper.map(savedAssignment, AssignmentDTO.class);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new InternalServerError("Error during creating assignment");
        }
    }


//    public ResponseEntity<CustomResponse> getAssignment(Long assignmentId, String type) throws IOException {
//        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new AssignmentNotFound("Assignment Not Found"));
//        boolean isFile = type == "audio" ? true : false;
//        Path path = Paths.get(isFile ? assignment.getFilePath() : assignment.getAudioPath());
//        String fileName = isFile ? assignment.getFileName() : assignment.getAudioFileName();
//        Resource resource = new UrlResource(path.toUri());
//        ContentDisposition contentDisposition = ContentDisposition.attachment()
//                .filename(fileName, StandardCharsets.UTF_8)
//                .build();
//
//        CustomResponse customResponse = new CustomResponse("Downloadable Data fetched Succesfully", assignment, HttpStatus.OK);
//        return ResponseEntity.status(HttpStatus.OK)
//                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
//                .body(customResponse);
//    }

    public Assignment getAssignmentById(Long assignmentId) {
        Assignment assignment = null;
        try {
            assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new AssignmentNotFound("Assignment Not Found"));
            return assignment;
        } catch (Exception e) {
            log.info("Exception in getAssignmentById" + e.getMessage());
            throw new RuntimeException();
        }
    }

    public SubmittedAssignment getSubmittedAssignmentById(Long assignmentId) {
        SubmittedAssignment assignment = null;

        assignment = submittedAssignmentRepository.findById(assignmentId).orElseThrow(() -> new AssignmentNotFound("Submitted Assignment Not Found"));
        return assignment;
    }

    public List<SubmittedAssignmentDTO> getSubmittedAssignmentByAssignmentId(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFound("Assignment Not Found"));

        return assignment.getSubmittedAssignments().stream()
                .map(this::toSubmittedAssignmentDto)
                .toList();
    }

    public List<AssignmentDTO> getAllAssignment(Long courseId, Authentication authentication) {
        CourseDTO course = courseService.getCourseByCourseId(courseId, authentication);
        List<Assignment> allAssignnment = assignmentRepository.findByCourseId(courseId);
        List<AssignmentDTO> assignmentDTOList = allAssignnment.stream().map(assignment -> modelMapper.map(assignment, AssignmentDTO.class)).toList();
        return assignmentDTOList;
    }

    public List<SubmittedAssignmentDTO> getSubmittedAssignments(Long assignmentId, Authentication authentication) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new AssignmentNotFound("Assignment Not with id {} Found"));
        List<SubmittedAssignment> submittedAssignments = assignment.getSubmittedAssignments();
        log.info("Submitted Assignments",submittedAssignments);
        List<SubmittedAssignmentDTO> submittedAssignmentsList = submittedAssignments.stream()
                .map(this::toSubmittedAssignmentDto)
                .toList();
        return submittedAssignmentsList;
    }

    private SubmittedAssignmentDTO toSubmittedAssignmentDto(SubmittedAssignment submission) {
        SubmittedAssignmentDTO dto = new SubmittedAssignmentDTO();
        if (submission.getAssignment() != null) {
            dto.setAssignmentId(submission.getAssignment().getId());
        }
        dto.setFileName(submission.getFileName());
        dto.setFilePath(submission.getFilePath());
        dto.setAudioFileName(submission.getAudioFileName());
        dto.setAudioPath(submission.getAudioPath());
        dto.setVideoFileName(submission.getVideoFileName());
        dto.setVideoPath(submission.getVideoPath());
        dto.setVttUrl(submission.getVttUrl());
        dto.setIsApproved(submission.getIsApproved());
        cloudinaryService.applyVideoDeliveryUrls(dto);
        return dto;
    }


    @Transactional
    public SubmittedAssignmentDTO approveAssignment(String assignmentId, Authentication authentication) {
        Long id = Long.parseLong(assignmentId);
        SubmittedAssignment assignment = getSubmittedAssignmentById(id);
        assignment.setIsApproved(true);
        try {
            log.info("ASSIGNMENT {}", assignment);
            submittedAssignmentRepository.save(assignment);
            try {
                notificationService.saveAndSendNotificationToPersonal("Assignment Approved", assignment.getSubmittedBy(), "/queue/assignment-updates", assignment);
            } catch (RuntimeException e) {
                logger.info("Error in Send Msg", e.getMessage());
                throw new RuntimeException(e);
            }
            SubmittedAssignmentDTO assignmentDTO = toSubmittedAssignmentDto(assignment);
            return assignmentDTO;
        } catch (RuntimeException e) {
            logger.info(e.getMessage());
            throw new InternalServerError("Unable to Approve Request");
        }

    }

    public Resource loadFileAsResource(Long assignmentId, FileType type) {
        SubmittedAssignment assignment = null;
        assignment = submittedAssignmentRepository.findById(assignmentId).orElseThrow(() -> new DataNotFound("Assignment Data Not Found for Files"));
        String filePath = switch (type) {
            case FileType.VIDEO -> assignment.getVideoPath();
            case FileType.AUDIO -> assignment.getAudioPath();
            default -> assignment.getFilePath();
        };
        if (filePath == null || filePath.isEmpty()) {
            throw new DataNotFound("No file path found for type: " + type);
        }
        return null;
    }
}
