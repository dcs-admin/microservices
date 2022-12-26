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
public class UserLoginStatus {

    @Id 
    private Long userNumber;
    private Date lastLoginAt;
    private boolean currentStatus;
    
}
