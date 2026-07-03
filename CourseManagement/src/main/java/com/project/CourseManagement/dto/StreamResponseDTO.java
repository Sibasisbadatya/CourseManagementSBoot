package com.project.CourseManagement.dto;

import jakarta.annotation.Resource;
import org.springframework.http.MediaType;

public class StreamResponseDTO {
    Resource resource;
    long start;
    long end;
    long fileSize;
    MediaType mediaType;
//    MediaType represents the Content-Type of the response
//            | File Type | MediaType                  |
//            | --------- | -------------------------- |
//            | MP4 video | `video/mp4`                |
//            | MP3 audio | `audio/mpeg`               |
//            | WAV audio | `audio/wav`                |
//            | PDF       | `application/pdf`          |
//            | Any file  | `application/octet-stream` |

}
