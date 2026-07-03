package com.project.CourseManagement.listeners;

import com.project.CourseManagement.entity.User;
import com.project.CourseManagement.events.NotificationEvent;
import com.project.CourseManagement.exception.NotificationError;
import com.project.CourseManagement.service.SmsService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final SmsService smsService;

    public NotificationListener(SimpMessagingTemplate messagingTemplate, SmsService smsService) {
        this.messagingTemplate = messagingTemplate;
        this.smsService = smsService;
    }

    @EventListener
    @Async
    public void handleSendInAppNotification(NotificationEvent event) {
        try {
            User user = event.getUser();
            String username = null;
            Object data = event.getData();
            String destination = event.getDestination();
            if (user == null) {
                throw new NotificationError("User Can't determined to whom to send notification");
            }
            if (WebSocketListeners.isOnline(user.getEmail())) {
                messagingTemplate.convertAndSendToUser(user.getEmail(), destination, data);
            } else {
                String mobileNo = "+91" + Long.toString(user.getMobileNo());
                smsService.sendSms(mobileNo, "Your Assignment Approved");
            }
//            Check if user is online
//            if online send in App Notification ,if not send to mobileNo
        } catch (MessagingException e) {
            throw new NotificationError(e.getMessage());
        }
    }
}
