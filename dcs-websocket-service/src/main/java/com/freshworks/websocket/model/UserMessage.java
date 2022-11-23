package com.freshworks.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMessage {

    private Long id;
    private String name;
    private String body;
    private String time;
    private boolean me; 
    private int unread;  
    
}
