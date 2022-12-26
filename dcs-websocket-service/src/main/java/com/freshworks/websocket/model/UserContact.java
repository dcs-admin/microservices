package com.freshworks.websocket.model;

import java.util.Date;

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
public class UserContact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long    id;

    private Long    userId;
    private Long    contactId;
    private String firstName;
    private String lastName; 

    private Date createdDate = new Date();
    private Date updatedDate;
    
}
