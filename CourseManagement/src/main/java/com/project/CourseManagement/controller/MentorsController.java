package com.project.CourseManagement.controller;

import com.project.CourseManagement.dto.CustomResponse;
import com.project.CourseManagement.dto.MentorDTO;
import com.project.CourseManagement.entity.Mentor;
import com.project.CourseManagement.service.MentorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/mentors")
public class MentorsController {

    private final MentorService mentorService;

    public MentorsController(MentorService mentorService) {
        this.mentorService = mentorService;
    }

    @GetMapping("/get-all-mentors")
    public ResponseEntity<CustomResponse> getAllMentors(@RequestParam int pageNo, @RequestParam int pageSize, @RequestParam List<String> sorts) {
        log.info("List of sorts {}",sorts);
        //mentors/get-all-mentors?pageNo=1&pageSize=10&sorts=rating:asc
//        keep this type of url because if we do sorts=rating,asc then Spring automatically treats commas (,)
//        as delimiters when binding a List<String> from query parameters. So instead of receiving ["rating,asc"],
//        the controller received ["rating", "asc"]
        List<Sort.Order> sortList = sorts
                .stream()
                .map((sort)->{
                    String[] split = sort.split(":");
                    log.info("Split of sort {}", (Object) split);
                    return new Sort.Order(Sort.Direction.fromString(split[1]), split[0]);
                })
                .toList();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortList));
        List<MentorDTO> mentors = mentorService.getAllMentors(pageable);
        CustomResponse customResponse = new CustomResponse<>("Mentors fetched Succesfully", mentors, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }

}
