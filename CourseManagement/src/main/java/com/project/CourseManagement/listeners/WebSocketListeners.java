package com.project.CourseManagement.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketListeners {
    private static final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    public static boolean isOnline(String username) {
        return onlineUsers.contains(username);
    }


    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getUser() != null) {
            String userName = accessor.getUser().getName();
            onlineUsers.add(userName);
        }

    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getUser() != null) {
            String userName = accessor.getUser().getName();
            onlineUsers.remove(userName);
        }
    }

}
