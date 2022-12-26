package com.freshworks.websocket.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.freshworks.websocket.model.User;
import com.freshworks.websocket.model.UserLoginStatus;
import com.freshworks.websocket.model.UserMessage;
import com.freshworks.websocket.util.ProducerMessageType;

@Service
public class StompMessageProducer {
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public boolean sendMessage(ProducerMessageType type, Object obj){

        boolean result = false;
        if(type ==  ProducerMessageType.MESSAGE){
            UserMessage userMessage = (UserMessage)obj;
            simpMessagingTemplate.convertAndSend("/topic/messages/"+userMessage.getToUser(), userMessage);
            result = true;
        } else if(type ==  ProducerMessageType.LOGIN){
            UserLoginStatus userLoginStatus = (UserLoginStatus)obj; 
            simpMessagingTemplate.convertAndSend("/topic/logins/"+userLoginStatus.getUserNumber(), userLoginStatus);
            System.out.println("LOGIN: "+"/topic/logins/"+userLoginStatus.getUserNumber());
            result = true;
        } else if(type ==  ProducerMessageType.LOGOUT){
            UserLoginStatus userLoginStatus = (UserLoginStatus)obj;  
            simpMessagingTemplate.convertAndSend("/topic/logins/"+userLoginStatus.getUserNumber(), userLoginStatus);
            System.out.println("LOGOUT: "+"/topic/logins/"+userLoginStatus.getUserNumber());
            result = true;
        }
        
        return result;
    }
}
