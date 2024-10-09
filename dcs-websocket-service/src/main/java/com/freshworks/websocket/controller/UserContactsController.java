package com.freshworks.websocket.controller;

 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import com.freshworks.websocket.model.MessageWrapper;
import com.freshworks.websocket.model.User;
import com.freshworks.websocket.model.UserContact;
import com.freshworks.websocket.model.UserContactDTO;
import com.freshworks.websocket.model.Conversation;
import com.freshworks.websocket.repo.UserContactRepository;
import com.freshworks.websocket.repo.UserMessageRepository;
import com.freshworks.websocket.repo.UserRepository;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/websocket")
public class UserContactsController { 
    // Annotation
    @Autowired 
    private UserContactRepository userContactRepository; 

    @Autowired 
    private UserRepository userRepository; 

    @Autowired 
    private UserMessageRepository userMessageRepository;
    
    
    @GetMapping("/userContacts/{id}")
    public MessageWrapper fetchUserContacts(@PathVariable("id") Long userId) {

        MessageWrapper messageWrapper = new MessageWrapper();
        List<UserContact> userContacts = userContactRepository.findByUserId(userId);

        userContacts.forEach( userContact -> {
            Conversation userContactMessage = new Conversation();
            userContactMessage.setUserContact(userContact); 
            userContactMessage.setMessages(userMessageRepository.findByFromUserOrToUserOrFromUserOrToUserOrderByCreatedDateDesc(userContact.getId(), userContact.getContactId(), userContact.getContactId(),  userContact.getId()));
            messageWrapper.getUserContactMessages().add(userContactMessage);
        });

        return messageWrapper;
    }


       
    @PostMapping("/userContacts")
    public UserContactDTO createUserContact(@RequestBody UserContactDTO userContactDto) {

        if(userRepository.findByUserNumber(userContactDto.getUserContact().getUserNumber()) == null){
            userRepository.save(userContactDto.getUserContact()); 
        }
        UserContact userContact = new UserContact();
        userContact.setUserId(userContactDto.getCurrentUserId());
        userContact.setContactId(userContactDto.getUserContact().getUserNumber()); //TODO: Need to rethink
        userContact.setFirstName(userContactDto.getUserContact().getFirstName());
        userContact.setLastName(userContactDto.getUserContact().getLastName()); 
        userContactRepository.save(userContact);  

        return userContactDto;
    }
    

    // @GetMapping("/conversations/{id}")
    // public MessageWrapper
    // getConversations(@PathVariable("id") Long userId) {
    //     return userMessageService.getConversations( userId);
    // } 

 } 
  
    
 