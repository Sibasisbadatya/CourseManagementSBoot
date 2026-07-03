package com.project.CourseManagement.config;

import com.project.CourseManagement.interceptors.WebSocketAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    public WebSocketConfig(WebSocketAuthInterceptor webSocketAuthInterceptor) {
        this.webSocketAuthInterceptor = webSocketAuthInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue");
//        🔹 enableSimpleBroker("/queue")
//        Spring provides a simple in-memory broker
//        Any destination starting with /queue → handled by broker
//        Clients subscribe → /queue/something
//        Server sends → broker distributes to all subscribers

//        enableSimpleBroker("/topic")
//        /topic → Publish–Subscribe (1 → many)
//        /queue → Point-to-Point (1 → 1 or specific user)


//        🔹 Visual Understanding
//        Case 1: /topic/chat
//
//👥 A, B, C subscribed
//➡️ Send message → A, B, C all get it
//
//        Case 2: /queue/messages (normal)
//
//👥 A, B, C subscribed
//➡️ Send message → A, B, C all get it
//✔️ Same as topic (simple broker behavior)
//
//        Case 3: /queue/messages + convertAndSendToUser
//
//👥 A, B, C exist
//➡️ Send to user A
//👉 Only A gets it

        registry.setApplicationDestinationPrefixes("/app");
//        there are two types of message flows:

//                | Type                 | Goes to                               |
//                | -------------------- | ------------------------------------- |
//                | `/app/...`           | **Backend (@MessageMapping methods)** |
//                | `/topic` or `/queue` | **Broker (direct to clients)**        |
//        registry.setApplicationDestinationPrefixes("/app");

//        It tells Spring:
//“If client sends message to /app/..., route it to my controller methods.”

//        Step 1: Client sends message
//        stompClient.send("/app/chat", {}, JSON.stringify(msg));

//        Step 2: Goes to Controller
//        @MessageMapping("/chat")
//        public void handleChat(Message msg) {
//            // process message
//        }
//        👉 /app/chat → mapped to @MessageMapping("/chat")
//
//            ✔  ️ /app is removed internally

    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
       registry.addEndpoint("/audio-ws").setAllowedOriginPatterns("http://localhost:[*]").withSockJS();

//       🔹 Part-by-part explanation
//        ✅ 1. addEndpoint("/audio-ws")
//        👉 This creates the WebSocket entry point
//        Client connects like:
//        const socket = new SockJS("http://localhost:8080/audio-ws");

//        👉 Think of it like:
//        REST → /api/users
//        WebSocket → /audio-ws


//        ✅ 2. setAllowedOriginPatterns("http://localhost:[*]")
        //👉 This is for CORS (security)
        //
        //        It means:
        //
        //        Allow connections from:
        //        http://localhost:3000
        //        http://localhost:5173
        //        any port on localhost
        //
        //✔️ Useful during development (React, Angular, etc.)

//        In production, you should restrict this:
//        .setAllowedOriginPatterns("https://yourdomain.com");

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketAuthInterceptor);
//        Messages flow like this:
//        Client → Server (Inbound) → Controller (@MessageMapping)
//        Inbound channel = messages coming from client to server

//        What does interceptors(...) do?
//        👉 It adds a filter/interceptor in the flow

//        Flow with interceptor
//        Client sends message
//          ↓
//        Interceptor (your logic runs here)
//          ↓
//        Controller (@MessageMapping)
    }
}
