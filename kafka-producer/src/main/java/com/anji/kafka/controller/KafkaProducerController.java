package com.anji.kafka.controller;

import com.anji.kafka.entity.ApiResponse;
import com.anji.kafka.entity.KafkaMessage;
import com.anji.kafka.entity.ModelProperties;
import com.anji.kafka.entity.SystemProperties;
import com.anji.kafka.scheduler.KafkaProduceSchedular;
import com.anji.kafka.service.ApplicationKafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

@RestController
@RequestMapping("/kafka-producer")
public class KafkaProducerController {

    @Autowired
    private ApplicationKafkaProducer applicationKafkaProducer;

    @Autowired
    private KafkaProduceSchedular kafkaProduceSchedular;

    @PostMapping("/singleMessage")
    public ResponseEntity<ApiResponse> sendMessage(@RequestBody String message) {
        applicationKafkaProducer.sendMessage(message);
        ApiResponse<String> response = new ApiResponse<>(
                "success",
                "Message processed successfully",
                message
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/objectMessage")
    public ResponseEntity<ApiResponse> sendMessage(@RequestBody KafkaMessage kafkaMessage) {
        try {
            // Send the message asynchronously
            Future<RecordMetadata> future = applicationKafkaProducer.sendMessage(kafkaMessage);

            // Wait for the result
            RecordMetadata metadata = future.get(); // Blocks until the operation is complete

            // Prepare the response
            String message = String.format("Message sent successfully to topic %s, partition %d, offset %d",
                    metadata.topic(), metadata.partition(), metadata.offset());

            ApiResponse<KafkaMessage> response = new ApiResponse<>(
                    "success",
                    message,
                    kafkaMessage
            );
            System.out.println(message);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(
                    "error",
                    "Something went wrong",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/sampleObjectMessage")
    public ResponseEntity<ApiResponse> produceSampelObjectMessage() {
        try {

            KafkaMessage kafkaMessage = new KafkaMessage();
            kafkaMessage.setKey("Key");
            kafkaMessage.setPartition(1);
            kafkaMessage.setPayloadType("PayloadType");
            kafkaMessage.setMessage("Test Message here");
            ModelProperties modelProperties = kafkaMessage.getModelProperties();
            modelProperties.setModelId("Key");
            modelProperties.setModelCategory("aaa");
            SystemProperties systemProperties = kafkaMessage.getSystemProperties();
            systemProperties.setSystemDescription("SystemDescription");
            systemProperties.setSystemId("SystemId");

            // Send the message asynchronously
            Future<RecordMetadata> future = kafkaProduceSchedular.produceObject(kafkaMessage);

            // Wait for the result
            RecordMetadata metadata = future.get(); // Blocks until the operation is complete

            // Prepare the response
            String message = String.format("Message sent successfully to topic %s, partition %d, offset %d",
                    metadata.topic(), metadata.partition(), metadata.offset());

            ApiResponse<KafkaMessage> response = new ApiResponse<>( "success", message, kafkaMessage);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(  "error", "Something went wrong", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
