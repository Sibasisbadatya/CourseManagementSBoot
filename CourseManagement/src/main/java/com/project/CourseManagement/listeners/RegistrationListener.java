package com.project.CourseManagement.listeners;

import com.project.CourseManagement.dto.UserDTO;
import com.project.CourseManagement.events.RegistrationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

public class RegistrationListener {

    private final JavaMailSender javaMailSender;

    public RegistrationListener(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    @EventListener
    public void handleUserRegistered(RegistrationEvent event) {

        SimpleMailMessage message = new SimpleMailMessage();
        UserDTO user = event.getUserDTO();
        message.setTo(user.getEmail());
        message.setSubject("Registration Successful");
        message.setText("Welcome " + user.getName());

        javaMailSender.send(message);
    }
//    @Async
//    public void sendEmail() { ... }

//    ou are telling Spring:
//            “Run this method in a different thread, not the main request thread

//    Normal Flow (Without Async)
//      Main Thread (HTTP Request)
//   → save user
//   → send email (2 sec delay 😴)
//   → return response


//    With @Async

//    Main Thread
//   → save user
//   → call async method
//   → return response immediately ✅
//
//    Worker Thread (Thread Pool)
//   → send email


//    2. How @Async Works Internally
//    This is the important part (interview level) 👇

//    Step 1: Spring Creates a PROXY
//    When Spring sees @Async, it doesn’t call your method directly.
//            Instead, it creates a proxy object (using AOP).

//    You call → Proxy → Actual Method

//    Step 2: Proxy Intercepts the Call
//
//    When you do:
//    emailService.sendEmail();
//    👉 Actually:
//      Proxy intercepts this call

//    Step 3: Proxy submits task to Thread Pool
//
//    Internally:
//            executor.submit(() -> actualMethod());
//    👉 That executor = ThreadPoolTaskExecutor

//    ThreadPool
//   → picks free thread
//   → runs your method



//    Use of  ThreadPoolTaskExecutor Here
//    ❌ Default Behavior
//    Spring uses:
//    SimpleAsyncTaskExecutor
//👉 Problem:
//
//    Creates new thread every time ❌
//    No reuse ❌
//    Not scalable ❌

//    So,We have to craete a config for customised threadpool

//    3. Why Separate Class is REQUIRED
//@Service
//public class UserService {
//
//    public void register() {
//        sendEmail(); // ❌ async will NOT work
//    }
//
//    @Async
//    public void sendEmail() { }
//}
//    👉 Why?
//
//    Because:
//
//    Call is internal (this.sendEmail())
//    Proxy is bypassed
//    So async is ignored ❌
}

