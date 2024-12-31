package com.anji.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class ConsumerPerformanceTest {

    private long startTime;
    private int messageCount = 0;
    private final int totalMessages;

    public ConsumerPerformanceTest(int totalMessages) {
        this.totalMessages = totalMessages;
    }

    @KafkaListener(topics = "${kafkaProperties.topics}", groupId = "foo")
    public void consume(ConsumerRecord<String, String> record) {
        if (messageCount == 0) {
            startTime = System.currentTimeMillis();
        }

        messageCount++;

        if (messageCount >= totalMessages) {
            long endTime = System.currentTimeMillis();
            System.out.println("Consumed " + totalMessages + " messages in " + (endTime - startTime) + " ms");
        }
    }
}