package com.freshworks.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContactDTO {
 
    private Long currentUserId = 0l;
    private User userContact = new User();
    
}
