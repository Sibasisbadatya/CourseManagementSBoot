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
//✅ Meaning:
//    A thread-safe Set storing usernames
//    Used to track online users
//🔥 Why ConcurrentHashMap.newKeySet()?
//    Multiple users connect/disconnect simultaneously
//    Needs thread safety (no race conditions)

    public static boolean isOnline(String username) {
        return onlineUsers.contains(username);
    }

//    SessionConnectEvent is fired when a client tries to connect to your WebSocket (STOMP) endpoint.
//when clint does
//    stompClient.connect({}, () => {
//        console.log("Connected");
//    });
//    At this moment → Spring fires SessionConnectEvent
    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
//        StompHeaderAccessor is a helper class in Spring that lets you read and modify STOMP message details
//        (headers, user, session, etc.)
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
