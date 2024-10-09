package com.anji.kafkademo.service;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.anji.kafkademo.model.Customer;

@Service
public class KafkaProducer {

    private static final String TOPIC = "my-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
