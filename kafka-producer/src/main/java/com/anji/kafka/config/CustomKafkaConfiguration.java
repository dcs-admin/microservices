package com.anji.kafka.config;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.kafka.shared.entity.KafkaMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class CustomKafkaConfiguration {

    @Bean
    public KafkaProducer<String, KafkaMessage> customKafkaProducer(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.kafka.shared.serializer.KafkaMessageSerializer");

        KafkaProducer<String, KafkaMessage> producer = new KafkaProducer<>(props);
        return producer;
    }
}
