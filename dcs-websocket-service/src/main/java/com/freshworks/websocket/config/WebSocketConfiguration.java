package com.freshworks.websocket.config;

import org.springframework.context.annotation.Configuration;
 
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
 

 

@Configuration
@EnableWebSocketMessageBroker 
@CrossOrigin
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket/stomp-endpoint")
        .setAllowedOriginPatterns("*") 
        .setHandshakeHandler(new UserHandler())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");

    } 

  
}
 
