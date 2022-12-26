package com.freshworks.websocket.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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
public class UserMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long fromUser;
    private Long toUser; 
    private String name;
    private String body;
    private String time;
    private boolean me; 
    private int unread;  

    private String proirity;

    @Enumerated(EnumType.STRING)
    private Imporatance importance = Imporatance.LOW;
    
    private Date createdDate;
    private Date updatedDate;
    
}
