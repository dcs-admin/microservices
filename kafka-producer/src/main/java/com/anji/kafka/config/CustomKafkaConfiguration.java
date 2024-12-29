package com.anji.kafka.config;

import com.anji.kafka.entity.KafkaMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
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
        props.put("value.serializer", "com.anji.kafka.serializer.KafkaMessageSerializer");

        KafkaProducer<String, KafkaMessage> producer = new KafkaProducer<>(props);
        return producer;
    }
}
