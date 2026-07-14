package com.project.CourseManagement.service;

import com.project.CourseManagement.entity.Assignment;
import com.project.CourseManagement.entity.SubmittedAssignment;
import com.project.CourseManagement.enums.FileType;
import com.project.CourseManagement.exception.DataNotFound;
import com.project.CourseManagement.exception.InternalServerError;
import com.project.CourseManagement.repository.AssignmentRepository;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StreamingService {

    private final AssignmentService assignmentService;
    private final AssignmentRepository assignmentRepository;


    public StreamingService(AssignmentService assignmentService, AssignmentRepository assignmentRepository) {
        this.assignmentService = assignmentService;
        this.assignmentRepository = assignmentRepository;
    }

    private MediaType getMediaType(String path) {
        if (path.endsWith(".mp4")) return MediaType.valueOf("video/mp4");
//        It takes a string like "application/json" and converts it into a MediaType object
//        Now type represents application/json

//        Why use it?
//                Because APIs work with MediaType objects, not raw strings.

//        headers.setContentType("application/json"); ❌
//        headers.setContentType(MediaType.valueOf("application/json")); ✅

//        Spring already provides constants
//        MediaType.APPLICATION_JSON
//        MediaType.TEXT_PLAIN
//        MediaType.APPLICATION_XML

//        So instead of:
//        MediaType.valueOf("application/json")

//        Prefer
//        MediaType.APPLICATION_JSON

//        type = application
//        subtype = json

        if (path.endsWith(".mp3")) return MediaType.valueOf("audio/mpeg");
        if (path.endsWith(".wav")) return MediaType.valueOf("audio/wav");
        if (path.endsWith(".pdf")) return MediaType.APPLICATION_PDF;
        else return MediaType.ALL;
    }


    private ResponseEntity<Resource> streamVideoAndAudio(
            Path filePath,
            String rangeHeader
    ) throws IOException {

        if (filePath == null) {
            throw new DataNotFound("File Not Found for this assignment");
        }

        File file = new File(filePath.toUri());
        long fileSize = file.length();
        if (rangeHeader == null) {
            return ResponseEntity.ok()
                    .contentType(getMediaType(String.valueOf(filePath)))
                    .contentLength(fileSize)
                    .body(new FileSystemResource(filePath));
        }

//        In Spring, Resource means:
//        “Something from which data can be read” can be image file video etc
//        Instead of asking:
//        is this from local disk?
//                is this from project?
//        is this from URL?
//        Spring simply says:
//
//        give me a Resource, I will read it.

//        | Class                | File comes from                       |
//        | -------------------- | ------------------------------------- |
//        | `ClassPathResource`  | inside project (`src/main/resources`) |
//        | `FileSystemResource` | from your computer disk               |
//        | `UrlResource`        | from internet URL                     |
//        | `ByteArrayResource`  | from byte[] in memory                 |


        String[] parts = rangeHeader.replace("bytes=", "").split("-");
        long start = Long.parseLong(parts[0]);
        long MAX_CHUNK_SIZE = 512 * 1024;
        long end = (parts.length > 1 && !parts[1].isEmpty()) ? Long.parseLong(parts[1]) : start + MAX_CHUNK_SIZE;
        if (end >= fileSize) end = fileSize - 1;
        long chunkSize = end - start + 1;

//        InputStream → reads file as a stream of bytes
//        FileInputStream is a Java class used to read data from a file as bytes
//        InputStream is = new FileInputStream("file.txt");
//        It reads raw binary data (0s and 1s)

//        InputStream (abstract)
//            ↑
//        FileInputStream

//        How it works?
//        int data = is.read();
//        Reads 1 byte at a time
//        Returns:
//        0–255 → byte value
//        -1 → end of file

//        try (FileInputStream fis = new FileInputStream("test.txt")) {
//            int data;
//            while ((data = fis.read()) != -1) {
//                System.out.print((char) data);
//            }
//        }

//        Important Methods
//        read()   int b = fis.read();
//        read(byte[] arr)   byte[] buffer = new byte[1024]; read 1024 at a time  fis.read(buffer); 👉 Faster (reads chunk)
//        close()   Releases file

//        Related Classes
//         1.BufferedInputStream
//         👉 Wraps FileInputStream for performance

//        BufferedInputStream bis = new BufferedInputStream(
//                new FileInputStream("file.txt")
//        );

//        2. FileOutputStream
//        Opposite → writes to file
//        FileOutputStream fos = new FileOutputStream("file.txt");

//        3. InputStreamReader
//        Converts bytes → characters
//        InputStreamReader reader = new InputStreamReader(fis);

//        4. BufferedReader
//        Reads text line by line
//        BufferedReader br = new BufferedReader(
//                new InputStreamReader(new FileInputStream("file.txt"))
//        );

//        8. Byte vs Character Stream
//                | Type             | Classes           | Use            |
//                | ---------------- | ----------------- | -------------- |
//                | Byte stream      | `FileInputStream` | images, videos |
//                | Character stream | `FileReader`      | text files     |

//        InputStream inputStream = new FileInputStream(filePath)  //older method
        InputStream inputStream;
        try {
            inputStream = Files.newInputStream(filePath);
            inputStream.skip(start);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }


//        Example
//        File size = 1MB
//        Browser requests:
//        Range: bytes=500000-600000
//        Without skip(start)
        //❌ You start reading from byte 0
        //❌ Wrong data sent
        //❌ Video breaks
//       With skip(start)
//        inputStream.skip(500000);
//        👉 Now reading starts from correct position ✅
//        File:  [0 ----------- 500000 ----------- 600000 ----------- end]
//        skip(500000)
//          ↑
//        start reading here

//        OTHER RELATED METHODS
//        --------------------------------------------------------
//        1.int data = inputStream.read();
//            👉 Reads 1 byte at a time

//        byte[] buffer = new byte[1024];
//        2.int bytesRead = inputStream.read(buffer);

//        👉 Reads chunk into buffer
//        👉 Returns:
//        number of bytes read
//        -1 if end of stream

//        3. read(byte[] buffer, int offset, int length)
//        inputStream.read(buffer, 0, 1024);
//        👉 Reads specific size into buffer

//        4. skip(long n)
//        inputStream.skip(5000);
//        👉 Skips n bytes (used in streaming)

//        5. available()
//        int availableBytes = inputStream.available();
//        Bytes that can be read without blocking
//        Not reliable for file size

//        6.close()
//        inputStream.close();
//        Always close to avoid memory leaks

        InputStreamResource resource = new InputStreamResource(inputStream);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
//                206 Partial Content
//                If you use 200 OK → streaming breaks (no seeking)
                .contentType(getMediaType(String.valueOf(filePath)))
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
//                sends Accept-Ranges: bytes
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize)
//                Content-Range: bytes 5000-10000/500000  //there must be a space after bytes
                .contentLength(chunkSize)
//                Content-Length: 5000
                .body(resource);
    }

    public ResponseEntity<Resource> streamFile(
            Long assignmentId,
            FileType type,
            String rangeHeader
    ) throws IOException {
        SubmittedAssignment assignment = assignmentService.getSubmittedAssignmentById(assignmentId);
        Path filePath = switch (type) {
            case FileType.VIDEO -> Path.of(assignment.getVideoPath());
            case FileType.AUDIO -> Path.of(assignment.getAudioPath());
            case FileType.FILE -> Path.of(assignment.getFilePath());
            default -> throw new InternalServerError("Invalid File Type Found");
        };

        if(type==FileType.VIDEO || type==FileType.AUDIO){
            return streamVideoAndAudio(filePath,rangeHeader);
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(new FileSystemResource(filePath));


    }

}
