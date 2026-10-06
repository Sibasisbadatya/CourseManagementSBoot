package com.project.CourseManagement.service;

import com.cloudinary.EagerTransformation;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.project.CourseManagement.config.CloudinaryConfig;
import com.project.CourseManagement.dto.CloudinaryVideoResponse;
import com.project.CourseManagement.dto.SubmittedAssignmentDTO;
import com.project.CourseManagement.exception.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Service
public class CloudinaryService {
    public static final int SPRITE_FRAME_WIDTH = 160;

    private static final Pattern TRANSFORMATION_SEGMENT =
            Pattern.compile("^(c|w|h|f|fl|so|vs|sp|q|du|eo|fps|b|ar|dpr|g|x|y|r|a|e|bo|l|u)_.*");

    private final CloudinaryConfig cloudinaryConfig;

    public CloudinaryService(CloudinaryConfig cloudinaryConfig) {
        this.cloudinaryConfig = cloudinaryConfig;
    }

    public String uploadFile(MultipartFile file, String folderName) throws FileStorageException {
        try {
            Map uploadResult =
                    cloudinaryConfig.cloudinaryClient()
                            .uploader()
                            .uploadLarge(
                                    file.getInputStream(),
                                    ObjectUtils.asMap(
                                            "resource_type", "auto",
                                            "folder", folderName,
                                            "chunk_size", 6 * 1024 * 1024
                                    )
                            );

            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            log.error("Error uploading file to Cloudinary: {}", e.getMessage());
            throw new FileStorageException("Error in uploading Files to cloudinary");
        }
    }

    public CloudinaryVideoResponse uploadVideo(MultipartFile file, String folderName) throws FileStorageException {
        try {
            Map<?, ?> uploadResult =
                    cloudinaryConfig.cloudinaryClient()
                            .uploader()
                            .uploadLarge(
                                    file.getInputStream(),
                                    ObjectUtils.asMap(
                                            "resource_type", "video",
                                            "folder", folderName,
                                            "chunk_size", 6 * 1024 * 1024,
                                            "eager", Arrays.asList(
                                                    // sp_auto cannot be combined with f_m3u8 in the same transformation
                                                    new EagerTransformation()
                                                            .streamingProfile("auto")
                                                            .format("m3u8"),
                                                    new EagerTransformation()
                                                            .crop("scale")
                                                            .width(SPRITE_FRAME_WIDTH)
                                                            .flags("sprite")
                                                            .format("jpg"),
                                                    new EagerTransformation()
                                                            .crop("scale")
                                                            .width(SPRITE_FRAME_WIDTH)
                                                            .flags("sprite")
                                                            .format("vtt")
                                            ),
                                            "eager_async", true
                                    )
                            );

            String publicId = (String) uploadResult.get("public_id");
            return CloudinaryVideoResponse
                    .builder()
                    .publicId(publicId)
                    .secureUrl((String) uploadResult.get("secure_url"))
                    .hlsUrl(getHlsUrl(publicId))
                    .vttUrl(getVttUrl(publicId))
                    .spriteUrl(getSpriteUrl(publicId))
                    .posterUrl(getPosterUrl(publicId))
                    .mp4Url(getMp4Url(publicId))
                    .duration(
                            uploadResult.get("duration") != null
                                    ? ((Number) uploadResult.get("duration")).doubleValue()
                                    : null
                    )
                    .build();
        } catch (IOException e) {
            log.error("Error uploading video to Cloudinary: {}", e.getMessage());
            throw new FileStorageException("Error in uploading Files to cloudinary");
        }
    }

    public String getHlsUrl(String publicId) {
        return cloudinaryConfig.cloudinaryClient()
                .url()
                .secure(true)
                .resourceType("video")
                .transformation(new Transformation().streamingProfile("auto"))
                .format("m3u8")
                .generate(publicId);
    }

    public String getMp4Url(String publicId) {
        return cloudinaryConfig.cloudinaryClient()
                .url()
                .secure(true)
                .resourceType("video")
                .format("mp4")
                .generate(publicId);
    }

    /**
     * Storyboard VTT used for seek/hover thumbnails.
     * Must use fl_sprite. vs_ (video_sampling) is only for GIF/WebP and returns HTTP 400 on .vtt.
     */
    public String getVttUrl(String publicId) {
        return cloudinaryConfig.cloudinaryClient()
                .url()
                .secure(true)
                .resourceType("video")
                .transformation(spriteTransformation())
                .format("vtt")
                .generate(publicId);
    }

    public String getSpriteUrl(String publicId) {
        return cloudinaryConfig.cloudinaryClient()
                .url()
                .secure(true)
                .resourceType("video")
                .transformation(spriteTransformation())
                .format("jpg")
                .generate(publicId);
    }

    public String getPosterUrl(String publicId) {
        return cloudinaryConfig.cloudinaryClient()
                .url()
                .secure(true)
                .resourceType("video")
                .transformation(
                        new Transformation()
                                .startOffset("0")
                                .crop("scale")
                                .width(640)
                )
                .format("jpg")
                .generate(publicId);
    }

    public void applyVideoDeliveryUrls(SubmittedAssignmentDTO dto) {
        if (dto == null) {
            return;
        }
        String publicId = extractPublicId(dto.getVideoPath());
        if (publicId == null) {
            publicId = extractPublicId(dto.getVttUrl());
        }
        if (publicId == null) {
            return;
        }
        dto.setHlsUrl(getHlsUrl(publicId));
        dto.setVttUrl(getVttUrl(publicId));
        dto.setSpriteUrl(getSpriteUrl(publicId));
        dto.setPosterUrl(getPosterUrl(publicId));
        dto.setMp4Url(getMp4Url(publicId));
    }

    public String extractPublicId(String cloudinaryUrl) {
        if (cloudinaryUrl == null || cloudinaryUrl.isBlank()) {
            return null;
        }
        String decoded = URLDecoder.decode(cloudinaryUrl, StandardCharsets.UTF_8);
        int uploadIdx = decoded.indexOf("/upload/");
        if (uploadIdx < 0) {
            return null;
        }
        String rest = decoded.substring(uploadIdx + "/upload/".length());
        int queryIdx = rest.indexOf('?');
        if (queryIdx >= 0) {
            rest = rest.substring(0, queryIdx);
        }
        String[] segments = rest.split("/");
        int start = 0;
        if (segments.length > 0 && isTransformationSegment(segments[0])) {
            start = 1;
        }
        if (start < segments.length && segments[start].matches("v\\d+")) {
            start++;
        }
        if (start >= segments.length) {
            return null;
        }
        String joined = String.join("/", Arrays.copyOfRange(segments, start, segments.length));
        int lastDot = joined.lastIndexOf('.');
        if (lastDot > 0) {
            joined = joined.substring(0, lastDot);
        }
        return joined.isBlank() ? null : joined;
    }

    private Transformation spriteTransformation() {
        return new Transformation()
                .crop("scale")
                .width(SPRITE_FRAME_WIDTH)
                .flags("sprite");
    }

    private boolean isTransformationSegment(String segment) {
        return segment.contains(",") || TRANSFORMATION_SEGMENT.matcher(segment).matches();
    }
}
