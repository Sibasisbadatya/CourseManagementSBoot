package com.project.CourseManagement.controller;

import com.project.CourseManagement.dto.CustomResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/audio")
public class AudioStreamController {
    private static final String AUDIO_PATH = "uploads/assignmentssiba@123/freesound_community-21-gun-salute-25444.mp3";
    private static final Logger logger = LoggerFactory.getLogger(AudioStreamController.class);

    @GetMapping("/stream")
    public ResponseEntity<Resource> streamAudio(@RequestHeader(value = "Range", required = false) String range) throws IOException {
        Path path = Paths.get(AUDIO_PATH);
        logger.info("PATH {}", path);
        try {
            long fileSize = Files.size(path);
            Resource resource = new UrlResource(path.toUri());
            long start = Long.parseLong(range.replace("bytes=", "").split("-")[0]);

            long chunkSize = 1024 * 1024;
            long end = Math.min(start + chunkSize - 1, fileSize - 1);
            InputStream inputStream = Files.newInputStream(path);
            logger.info("inputStream {}", path);
            inputStream.skip(start);
            byte[] data = inputStream.readNBytes((int) (end - start + 1));
            ByteArrayResource chunk = new ByteArrayResource(data);
            logger.info("CHUNK {}", chunk);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(MediaType.valueOf("audio/mpeg"))
                    .header(HttpHeaders.CONTENT_RANGE,
                            "bytes " + start + "-" + end + "/" + fileSize)
                    .contentLength(data.length)
                    .body(chunk);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
