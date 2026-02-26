package com.project.CourseManagement.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class AudioSocketController {

    @MessageMapping("/play-audio")
    @SendTo("/queue/get-audio")
    public String handleAudioRequest(String message) {
        System.out.println("Received request: " + message);
        return message;
    }
}
