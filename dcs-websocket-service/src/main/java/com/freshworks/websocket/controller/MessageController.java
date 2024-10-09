package com.freshworks.websocket.controller;

 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
 
 
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;
 
import com.freshworks.websocket.model.HelloMessage;
import com.freshworks.websocket.model.MessageWrapper;
import com.freshworks.websocket.model.UserMessage;
import com.freshworks.websocket.service.UserMessageService;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/websocket")
public class MessageController { 
    // Annotation
    @Autowired 
    private UserMessageService userMessageService;

    // @MessageMapping("/hello")
    // @SendTo("/topic/greetings")
    // public Greeting greet(HelloMessage message) throws InterruptedException { 
    //     return new Greeting("Hello, " +  HtmlUtils.htmlEscape(message.getName()));
    // } 
 
     @PostMapping("/userMessages")
     public UserMessage saveUserMessage( @RequestBody UserMessage userMessage){
         return userMessageService.saveUserMessage(userMessage);
     }  
 

     @GetMapping("/userMessages")
     public List<UserMessage> fetchUserMessageList() {
         return userMessageService.fetchUserMessageList();
     }
   
     @PutMapping("/userMessages/{id}")
     public UserMessage
     updateUserMessage(@RequestBody UserMessage userMessage, @PathVariable("id") Long userMessageId) {
         return userMessageService.updateUserMessage(
             userMessage, userMessageId);
     } 


     @DeleteMapping("/userMessages/{id}")
     public String deleteUserMessageById(@PathVariable("id") Long userMessageId) {
         userMessageService.deleteUserMessageById( userMessageId);
         return "Deleted Successfully";
     }
 } 
  
    
 