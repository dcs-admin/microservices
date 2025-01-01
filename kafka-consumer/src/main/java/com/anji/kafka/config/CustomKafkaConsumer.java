package com.anji.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.kafka.shared.entity.KafkaMessage;
import com.anji.kafka.service.GenericService;
import com.anji.kafka.service.factory.ServiceFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.listener.ConsumerSeekAware;

import java.util.Map;

@Configuration
@EnableKafka
public class CustomKafkaConsumer implements ConsumerSeekAware {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ServiceFactory serviceFactory;
    private long startTime;
    private int messageCount = 0;
    private final int totalMessages = 1000;

    //private long startOffset = 201695;


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(10); // Increase concurrency
        factory.getContainerProperties().setPollTimeout(3000);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL); // Manual commit
        return factory;
    }

    @KafkaListener(topics = "${kafkaProperties.topics}", groupId = "foo")
    public void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        String message = record.value();
        try {
            KafkaMessage kafkaMessage = objectMapper.readValue(message, KafkaMessage.class);
            System.out.println("Received PayloadType:" + kafkaMessage.getPayloadType().toLowerCase() + "; Offset: " + record.offset());
            // Route message to individual service
            GenericService genericService = serviceFactory.getService(kafkaMessage.getPayloadType());
            if (genericService != null) {
                genericService.process(kafkaMessage);
            } else {
                System.out.println("No service found for the message type");
            }
            acknowledgment.acknowledge();

            if (messageCount == messageCount%1000) {
                startTime = System.currentTimeMillis();
            }

            messageCount++;

            if (messageCount >= totalMessages) {
                long endTime = System.currentTimeMillis();
                System.out.println("BenchmarkReport: Consumed " + totalMessages + " messages in " + (endTime - startTime) + " ms");
            }

        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
//        for (TopicPartition partition : assignments.keySet()) {
//            callback.seek(partition.topic(), partition.partition(), startOffset);
//        }
    }

    @Override
    public void registerSeekCallback(ConsumerSeekCallback callback) {
        // No implementation needed for this example
    }

    @Override
    public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        // No implementation needed for this example
    }


}
