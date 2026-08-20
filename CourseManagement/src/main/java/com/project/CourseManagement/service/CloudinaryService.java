package com.project.CourseManagement.service;

import com.cloudinary.utils.ObjectUtils;
import com.project.CourseManagement.config.CloudinaryConfig;
import com.project.CourseManagement.exception.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class CloudinaryService {
    private final CloudinaryConfig cloudinaryConfig;

    public CloudinaryService(CloudinaryConfig cloudinaryConfig) {
        this.cloudinaryConfig = cloudinaryConfig;
    }


    public String uploadFile(MultipartFile file, String folderName) throws FileStorageException {
        try {
            Map uploadResult =
                    cloudinaryConfig.cloudinaryClient()
                            .uploader()
                            .upload(
                                    file.getBytes(),
                                    ObjectUtils.asMap(
                                            "resource_type", "auto",
                                            "folder", folderName
                                    )
                            );

            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            log.info("Error uploading to CLoudinary: {}", e.getMessage());
            throw new FileStorageException("Error in uploading Files to cloudinary");
        }
    }


}
