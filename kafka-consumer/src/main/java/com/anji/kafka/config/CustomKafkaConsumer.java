package com.anji.kafka.config;

import org.kafka.shared.entity.KafkaMessage;
import com.anji.kafka.service.GenericService;
import com.anji.kafka.service.factory.ServiceFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class CustomKafkaConsumer {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ServiceFactory serviceFactory;


//    @Value("${myapplication.topic}")
//    private String TOPIC;

    @KafkaListener(topics = "${kafkaProperties.topics}", groupId = "foo")
    public void consume(String message) {
        try {
            KafkaMessage kafkaMessage = objectMapper.readValue(message, KafkaMessage.class);
            System.out.println("Received PayloadType:"+kafkaMessage.getPayloadType().toLowerCase());
            // Route message to individual service
           GenericService genericService = serviceFactory.getService(kafkaMessage.getPayloadType());
           if(genericService != null){
               genericService.process(kafkaMessage);
           }else{
                System.out.println("No service found for the message type");
           }

        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
