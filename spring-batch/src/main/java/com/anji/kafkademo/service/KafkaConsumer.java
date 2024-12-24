package com.anji.kafkademo.service;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.anji.kafkademo.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// @Service
public class KafkaConsumer {

    private ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "my-topic", groupId = "foo")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);

        
        try {
            Customer customer = objectMapper.readValue(message, Customer.class);
            System.out.println("The Converted Object Email:"+customer.getEmail());
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
