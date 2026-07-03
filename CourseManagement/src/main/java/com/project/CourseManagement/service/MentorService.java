package com.project.CourseManagement.service;

import com.project.CourseManagement.dto.MentorDTO;
import com.project.CourseManagement.entity.Mentor;
import com.project.CourseManagement.repository.MentorRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MentorService {
    private final MentorRepository mentorRepository;
    private final ModelMapper modelMapper;

    public MentorService(MentorRepository mentorRepository, ModelMapper modelMapper) {
        this.mentorRepository = mentorRepository;
        this.modelMapper = modelMapper;
    }

    public List<MentorDTO> getAllMentors(Pageable pageable) {
        List<Mentor> mentorsList = mentorRepository.findAll();
        log.info("Mentors List: {}", mentorsList);
        Page<Mentor> mentors = mentorRepository.findAll(pageable);

        return mentors
                .stream()
                .map(mentor -> modelMapper.map(mentor, MentorDTO.class))
                .collect(Collectors.toList());
    }
}
