package org.kafka.shared.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.kafka.shared.entity.KafkaMessage;

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
            throw new RuntimeException("Error serializing KafkaMessage", e);
        }
    }

    @Override
    public void close() {
        // No resources to clean up
    }
}
