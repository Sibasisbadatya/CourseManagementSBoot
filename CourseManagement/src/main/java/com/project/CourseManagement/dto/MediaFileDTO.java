package com.project.CourseManagement.dto;

import com.project.CourseManagement.enums.FileType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MediaFileDTO {
    @Enumerated(EnumType.STRING)
    private FileType type;
    private String content;

    public MediaFileDTO(FileType type, String content) {
        this.type = type;
        this.content = content;
    }
}
