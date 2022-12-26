package com.freshworks.websocket.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id; 

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long    uid;
    
    @Column(nullable = false, unique = true, length = 10)
    private Long    userNumber;
    private String firstName;
    private String lastName;
   // @Column(nullable = false, length = 64) 
  
    private String password;
    private String address;
    private String remarks;
    private String role;
    private String permission;
    private String gender;

    private Date createdDate;
    private Date updatedDate;

    //@Column(nullable = true, unique = true, length = 45)
    private String email;
    
}
