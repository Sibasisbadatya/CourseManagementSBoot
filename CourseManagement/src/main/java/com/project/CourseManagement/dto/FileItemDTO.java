package com.project.CourseManagement.dto;

import com.project.CourseManagement.enums.FileType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FileItemDTO {
    @Enumerated(EnumType.STRING)
    private FileType type;
    private String fileName;

    public FileItemDTO(FileType type, String fileName) {
        this.type = type;
        this.fileName = fileName;
    }
}
