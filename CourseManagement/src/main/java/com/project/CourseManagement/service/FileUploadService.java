package com.project.CourseManagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileUploadService {

    private static final Logger log = LoggerFactory.getLogger(FileUploadService.class);

    // Define a root directory path constant
    private final Path rootProfileDir = Paths.get("profileImages").toAbsolutePath().normalize();

    public String uploadUserProfileImage(MultipartFile file, String userEmail) throws IOException {
        // Guard clause: Make sure a file was actually sent
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // 1. Create the user-specific directory path safely
            // Using createDirectories() avoids "FileAlreadyExistsException" if the folder exists
            Path userDirectory = this.rootProfileDir.resolve(userEmail);
            Files.createDirectories(userDirectory);

            // 2. Extract and sanitize the filename
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path targetLocation = userDirectory.resolve(fileName);

            // 3. Stream and write the file bytes to the local disk location
            // REPLACE_EXISTING ensures updates don't trigger crashes
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            log.info("Successfully saved profile image for user: {}", userEmail);

            // Return the relative path string so you can save it in the User Database Column later!
            return "profileImages/" + userEmail + "/" + fileName;

        } catch (IOException e) {
            log.error("Fatal I/O storage failure for user context [{}]: {}", userEmail, e.getMessage());
            throw new IOException("Could not store file system asset. Please try again!", e);
        }
    }
}