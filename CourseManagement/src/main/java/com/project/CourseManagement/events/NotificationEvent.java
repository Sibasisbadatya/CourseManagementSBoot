package com.project.CourseManagement.events;

import com.project.CourseManagement.entity.User;
import lombok.Data;
//
//@Transactional
//public void saveAndSendNotificationToPersonal(String message, User user, String destination, Object data) {
//    SavedNotification savedNotification = new SavedNotification(message, user, false);
//    try {
//        notificationRepository.save(savedNotification);
//    } catch (RuntimeException e) {
//        throw new InternalServerError("Error in Saving Notification");
//    }
//    String username = user.getEmail();
//    try {
//        if (WebSocketListeners.isOnline(username)) {
//            messagingTemplate.convertAndSendToUser(user.getEmail(), destination, data);
//        } else {
//            String mobileNo = "+91" + Long.toString(user.getMobileNo());
//            smsService.sendSms(mobileNo, "Your Assignment Approved");
//        }

/// /            Check if user is online
/// /            if online send in App Notification ,if not send to mobileNo
//    } catch (MessagingException e) {
//        throw new NotificationError(e.getMessage());
//    }
//
//}

@Data
public class NotificationEvent {
    private final String destination;
    private final User user;
    private final Object data;

    public User getUser() {
        return user;
    }

    public String getDestination() {
        return destination;
    }

    public Object getData() {
        return data;
    }

    public NotificationEvent(User user, String destination, Object data) {
        this.destination = destination;
        this.data = data;
        this.user = user;
    }
}
