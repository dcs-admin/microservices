package com.anji.kafka.service;

import org.kafka.shared.entity.KafkaMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class ApplicationKafkaProducer {

    @Value("${myapplication.topic}")
    private String TOPIC ;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private KafkaProducer<String, KafkaMessage> producer;

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
    }

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

    public void sendMessage(String topic, String key, String message) {
        kafkaTemplate.send(topic, key, message);
    }

    public void sendMessage(String topic, Integer partition, String key, String message) {
        kafkaTemplate.send(topic, partition, key, message);
    }

    public void sendMessage(String topic, Integer partition, Long timestamp, String key, String message) {
        kafkaTemplate.send(topic, partition, timestamp, key, message);
    }


    public Future<RecordMetadata> sendMessage(KafkaMessage kafkaMessage){
        Future<RecordMetadata> meta = producer.send(new org.apache.kafka.clients.producer.ProducerRecord<>(TOPIC, kafkaMessage.getKey(), kafkaMessage));
        return meta;
    }
}
