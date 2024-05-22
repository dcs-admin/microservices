package com.anji.approvals.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {

    private List<Choice> choices;

    // constructors, getters and setters
    
    public static class Choice {

        private int index;
        private Message message;
        public Message getMessage() {
            return null;
        }

        // constructors, getters and setters
    }
}
