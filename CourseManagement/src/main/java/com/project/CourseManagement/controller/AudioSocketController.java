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

//    Step-by-step Flow
//✅ 1. Client sends message
//    stompClient.send("/app/play-audio", {}, "audio-data");
//Because of:
//    setApplicationDestinationPrefixes("/app")

//    2. Controller receives it
//    @MessageMapping("/play-audio")
//    /app/play-audio → mapped here
//   👉 message = "audio-data"


//    4. Return value is sent automatically
//    @SendTo("/queue/get-audio")
//    stompClient.subscribe("/queue/get-audio", callback);

//    Clients receive it
//    stompClient.subscribe("/queue/get-audio", callback);
//    All subscribers to /queue/get-audio get the message

//    this is equivalent to
//    --------------------------
//    @MessageMapping("/play-audio")
//    public void handleAudioRequest(String message) {
//        messagingTemplate.convertAndSend("/queue/get-audio", message);
//    }

}

