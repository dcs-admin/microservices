package com.anji.kafkademo.service;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "my-topic", groupId = "foo")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }
}
