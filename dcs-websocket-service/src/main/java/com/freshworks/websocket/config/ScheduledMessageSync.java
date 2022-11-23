package com.freshworks.websocket.config;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.freshworks.websocket.model.Greeting;
import com.freshworks.websocket.model.HelloMessage;
import com.freshworks.websocket.model.UserMessage;

@Configuration
@EnableScheduling 
public class ScheduledMessageSync {
 
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private long NUMBER =1;
 
    
    //@Scheduled(fixedDelay = 5000)
    public void scheduleFixedDelayTask() throws Exception {
        System.out.println( "Fixed delay task - " + System.currentTimeMillis() / 1000);
        HelloMessage message = new HelloMessage();
        message.setName(""+(NUMBER++));
        this.sendMessage(message );

        this.sendPrivateMessage(message );
    } 

    

    private void sendMessage(HelloMessage message) throws Exception { 
        simpMessagingTemplate.convertAndSend("/topic/status2" , new Greeting( message.getName())); 
    }

    private void sendPrivateMessage(HelloMessage message) throws Exception { 
        simpMessagingTemplate.convertAndSendToUser("Test","/user/topic/status2" , new Greeting( message.getName())); 
    }



    @Scheduled(fixedDelay = 5000)
    public void scheduleMessagesToWhatsapp() throws Exception {
        System.out.println( "Fixed Whatsapp delay task - " + System.currentTimeMillis() / 1000);
       UserMessage  userMessage = new UserMessage();
       long randomNum = ThreadLocalRandom.current().nextLong(1l, 14l );
       userMessage.setId(randomNum );
       userMessage.setBody("Automated Message");
       userMessage.setTime(""+new Date()); 
       userMessage.setUnread(0);
        
       simpMessagingTemplate.convertAndSend("/topic/messages" , userMessage); 

    }

}