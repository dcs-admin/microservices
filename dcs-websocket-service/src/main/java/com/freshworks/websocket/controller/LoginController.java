package com.freshworks.websocket.controller;

 

import org.springframework.beans.factory.annotation.Autowired;
 
 
 
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RestController;
 
import com.freshworks.websocket.model.User;
import com.freshworks.websocket.model.UserLoginStatus;
import com.freshworks.websocket.repo.UserLoginStatusRepository;
import com.freshworks.websocket.repo.UserRepository;
import com.freshworks.websocket.service.StompMessageProducer;
import com.freshworks.websocket.util.ProducerMessageType;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@CrossOrigin
public class LoginController { 
     
    @Autowired 
    private UserRepository userRepository;

    @Autowired 
    private UserLoginStatusRepository userLoginStatusRepository;


    //@Autowired 
    //private BCryptPasswordEncoder passwordEncoder;

    @Autowired 
    private StompMessageProducer stompMessageProducer;
 
     @PostMapping("/users")
     public User createUser( @RequestBody User user) throws Exception{ 
        System.out.println("User: "+user);
        //String encodedPassword = passwordEncoder.encode(user.getPassword());
        String encodedPassword = user.getPassword(); 
        user.setPassword(encodedPassword);
        return userRepository.save(user);
     }
 

     @GetMapping("/users")
     public List<User> fetchUserList() {
         return userRepository.findAll();
     }
   
     @PutMapping("/users/{id}")
     public Optional<User> updateUser(@RequestBody User user, @PathVariable("id") Long userId) {
         return userRepository.findById(userId);
     } 


     @DeleteMapping("/users/{id}")
     public String deleteUserById(@PathVariable("id") Long userId) {
         userRepository.deleteById(userId);
         return "Deleted Successfully";
     }


     @PostMapping("/users/login")
     public User login( @RequestBody User user) throws Exception{ 

        User resUser = userRepository.findByUserNumber(user.getUserNumber());
        if(resUser == null){
            throw new Exception("Unable to find mobile: "+user.getUserNumber());
        }else{
            //if(passwordEncoder.matches(user.getPassword(), resUser.getPassword())){
            if( user.getPassword().equals(resUser.getPassword())){
                UserLoginStatus userLoginStatus = new UserLoginStatus(user.getUserNumber(), new Date(), true); 
                userLoginStatusRepository.save(userLoginStatus);
                stompMessageProducer.sendMessage( ProducerMessageType.LOGIN, userLoginStatus);
                return resUser;
            }else{
                throw new Exception("Incorrect password for mobile: "+user.getUserNumber());
            }
        } 
     } 


     @GetMapping("/users/logout/{id}")
     public void logout( @PathVariable("id") Long userId) throws Exception{ 

        User resUser = userRepository.findByUserNumber(userId);
        if(resUser == null){
            throw new Exception("Unable to find mobile: "+userId);
        }else{ 
            UserLoginStatus userLoginStatus = new UserLoginStatus(userId, new Date(), false); 
            userLoginStatusRepository.save(userLoginStatus);
            stompMessageProducer.sendMessage( ProducerMessageType.LOGOUT, userLoginStatus); 
        } 
     } 


     @GetMapping("/users/getstatus/{id}")
     public UserLoginStatus getStatus( @PathVariable("id") Long userId) throws Exception{ 

        Optional<UserLoginStatus> userLoginStatus = userLoginStatusRepository.findById(userId);
        if(userLoginStatus.isEmpty()){
         return new UserLoginStatus(userId, new Date(), false);  
        }else{ 
         return userLoginStatus.get();
        } 
     } 

 } 
  
    
 