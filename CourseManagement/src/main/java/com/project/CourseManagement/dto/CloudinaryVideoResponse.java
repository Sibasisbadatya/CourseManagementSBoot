package com.project.CourseManagement.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudinaryVideoResponse {
    private String publicId;
    private String secureUrl;
    private String hlsUrl;
    private String mp4Url;
    private String vttUrl;
    private String spriteUrl;
    private String posterUrl;
    private Double duration;
}
