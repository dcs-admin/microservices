package com.freshworks.websocket.controller;

 

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
 
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.HtmlUtils;

import com.freshworks.websocket.model.Greeting;

import com.freshworks.websocket.model.HelloMessage;

@Controller
@CrossOrigin
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greet(HelloMessage message) throws InterruptedException {
        
        return new Greeting("Hello, " +
                HtmlUtils.htmlEscape(message.getName()));
    }

 
  
    
}
