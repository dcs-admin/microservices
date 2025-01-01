package com.anji.kafka.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.kafka.shared.entity.KafkaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class ProducerPerformanceTest {

    @Value("${myapplication.topic}")
    private String TOPIC;


    @Autowired
    private ApplicationKafkaProducer applicationKafkaProducer;

    public void runBenchmark(int messageCount) {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < messageCount; i++) {
            String message = "Message " + i;
            Future<RecordMetadata> future = applicationKafkaProducer.sendMessage(message);
            try {
                future.get(); // Ensure the message is sent
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Sent " + messageCount + " messages in " + (endTime - startTime) + " ms");
    }
}