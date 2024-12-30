package com.anji.kafka.scheduler;

import org.kafka.shared.entity.KafkaMessage;
import org.kafka.shared.entity.ModelProperties;
import org.kafka.shared.entity.SystemProperties;
import com.anji.kafka.service.ApplicationKafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
public class KafkaProduceSchedular {


    @Autowired
    private ApplicationKafkaProducer kafkaProducer;

//    @Scheduled(fixedRate = 20*1000)
    public void produceMessage(){
        kafkaProducer.sendMessage("TEST MESSAGE : "+System.currentTimeMillis());
        System.out.println("TEXT Message sent successfully");
    }


//    @Scheduled(fixedRate = 2000)
    public Future<RecordMetadata> produceObject(KafkaMessage kafkaMessage ){
        Future<RecordMetadata> response = kafkaProducer.sendMessage(kafkaMessage);
        System.out.println("Object Message sent successfully");
        return response;
    }


}
