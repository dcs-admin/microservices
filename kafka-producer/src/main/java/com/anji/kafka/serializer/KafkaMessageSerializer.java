package com.anji.kafka.serializer;

import com.anji.kafka.entity.KafkaMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class KafkaMessageSerializer implements Serializer<KafkaMessage> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No configuration needed
    }

    @Override
    public byte[] serialize(String topic, KafkaMessage data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error serializing KafkaMessage", e);
        }
    }

    @Override
    public void close() {
        // No resources to clean up
    }
}
