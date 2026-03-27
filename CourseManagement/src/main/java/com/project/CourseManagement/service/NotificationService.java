package com.project.CourseManagement.service;

import com.project.CourseManagement.entity.SavedNotification;
import com.project.CourseManagement.entity.User;
import com.project.CourseManagement.events.NotificationEvent;
import com.project.CourseManagement.exception.InternalServerError;
import com.project.CourseManagement.exception.NotificationError;
import com.project.CourseManagement.listeners.WebSocketListeners;
import com.project.CourseManagement.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final SmsService smsService;
    private final ApplicationEventPublisher publisher;

    public NotificationService(SimpMessagingTemplate messagingTemplate, NotificationRepository notificationRepository, SmsService smsService, WebSocketListeners webSocketListeners, ApplicationEventPublisher publisher) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
        this.smsService = smsService;
        this.publisher = publisher;
    }


    @Transactional
    public void saveAndSendNotificationToPersonal(String message, User user, String destination, Object data) {
        SavedNotification savedNotification = new SavedNotification(message, user, false);
        try {
            log.info("SAving Notification");
            notificationRepository.save(savedNotification);
        } catch (RuntimeException e) {
            throw new InternalServerError("Error in Saving Notification");
        }
        String username = user.getEmail();
        try {
            log.info("Publishing Event");

//            if (WebSocketListeners.isOnline(username)) {
//                messagingTemplate.convertAndSendToUser(user.getEmail(), destination, data);
//            } else {
//                String mobileNo = "+91" + Long.toString(user.getMobileNo());
//                smsService.sendSms(mobileNo, "Your Assignment Approved");
//            }
            NotificationEvent notificationEvent = new NotificationEvent(user, destination, data);
            publisher.publishEvent(notificationEvent);
//            Check if user is online
//            if online send in App Notification ,if not send to mobileNo
        } catch (MessagingException e) {
            throw new NotificationError(e.getMessage());
        }

    }
}
