package com.freshworks.websocket.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshworks.websocket.model.UserMessage;
import com.freshworks.websocket.repo.UserMessageRepository;
import com.freshworks.websocket.util.ProducerMessageType;

 

@Service
public class UserMessageService {
    
    @Autowired
    private UserMessageRepository userMessageRepository;

    @Autowired
    private StompMessageProducer stompMessageProducer;

   
 
    // Save operation 
    public UserMessage saveUserMessage(UserMessage userMessage)
    {   userMessage.setId(null);
        userMessage.setCreatedDate(new Date());
        userMessage = userMessageRepository.save(userMessage);
        
        stompMessageProducer.sendMessage( ProducerMessageType.MESSAGE, userMessage);

        return userMessage;
        
    }
 
    // Read operation
     public List<UserMessage> fetchUserMessageList()
    {
        return (List<UserMessage>)
            userMessageRepository.findAll();
    }
 
    // Update operation
    
    // public UserMessage
    // updateUserMessage(UserMessage userMessage,
    //                  Long userMessageId)
    // {
    //     UserMessage depDB
    //         = userMessageRepository.findById(userMessageId)
    //               .get();
 
    //     if (Objects.nonNull(userMessage.getUserMessageName())
    //         && !"".equalsIgnoreCase(
    //             userMessage.getUserMessageName())) {
    //         depDB.setUserMessageName(
    //             userMessage.getUserMessageName());
    //     }
 
    //     if (Objects.nonNull(
    //             userMessage.getUserMessageAddress())
    //         && !"".equalsIgnoreCase(
    //             userMessage.getUserMessageAddress())) {
    //         depDB.setUserMessageAddress(
    //             userMessage.getUserMessageAddress());
    //     }
 
    //     if (Objects.nonNull(userMessage.getUserMessageCode())
    //         && !"".equalsIgnoreCase(
    //             userMessage.getUserMessageCode())) {
    //         depDB.setUserMessageCode(
    //             userMessage.getUserMessageCode());
    //     }
 
    //     return userMessageRepository.save(depDB);
    // }
 
    // Delete operation
    
    public void deleteUserMessageById(Long userMessageId)
    {
        userMessageRepository.deleteById(userMessageId);
    }

    public UserMessage updateUserMessage(UserMessage userMessage, Long userMessageId) {
        return null;
    }


    public void getConversations(Long userId){

    }
}
