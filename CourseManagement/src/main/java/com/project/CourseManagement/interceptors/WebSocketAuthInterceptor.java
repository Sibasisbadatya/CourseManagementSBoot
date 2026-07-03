package com.project.CourseManagement.interceptors;

import com.project.CourseManagement.security.CustomUserDetailService;
import com.project.CourseManagement.utils.JWTUtils;
import org.springframework.messaging.*;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JWTUtils jwtUtils; // utility to parse JWT token
    private final CustomUserDetailService userDetailService; // service to load user from DB

    // constructor injection
    public WebSocketAuthInterceptor(JWTUtils jwtService, CustomUserDetailService userDetailService) {
        this.jwtUtils = jwtService;
        this.userDetailService = userDetailService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        // extract STOMP-related info from message (headers, command, user, etc.)
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

//        Why do we need this?

//      👉 message is a generic Spring Message object
//        Hard to directly read headers/user ❌
//      👉 So we convert it into:
//        StompHeaderAccessor → easy access

//        What this line does internally
//        Takes raw message
//        Checks if it contains STOMP info
//        Wraps it into StompHeaderAccessor

        // run this logic only when client is trying to CONNECT
        if (StompCommand.CONNECT.equals(accessor.getCommand())) { //accessor.getCommand(); // CONNECT, SEND, SUBSCRIBE

            // get Authorization header sent from frontend
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            // check if header exists and starts with "Bearer "
            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                // remove "Bearer " prefix → get actual JWT token
                String token = authHeader.substring(7);

                // extract username from JWT token
                String username = jwtUtils.extractUserNameFromToken(token);

                // load full user details (roles, authorities, etc.) from DB
                UserDetails userDetails = userDetailService.loadUserByUsername(username);

                // create authentication object (Spring Security)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // attach authenticated user to WebSocket session
                // now accessor.getUser() will work everywhere
                accessor.setUser(authentication);
            }
        }

        // return message so it continues to next step (controller, etc.)
        return message;
    }
}