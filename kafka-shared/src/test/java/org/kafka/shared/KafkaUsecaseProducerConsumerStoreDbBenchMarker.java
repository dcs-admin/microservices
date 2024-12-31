package org.kafka.shared;

import org.kafka.shared.entity.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaUsecaseProducerConsumerStoreDbBenchMarker {

    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/kafka-producer/objectMessage";

        Random random = new Random();

        ExecutorService executor = Executors.newFixedThreadPool(10);
        int messageCount = 100;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < messageCount; i++) {
            //executor.execute(() -> {
                produceObjectMessage(random, restTemplate, url);
            //});
        }
        long endTime = System.currentTimeMillis();
        System.out.println("BenchmarkReport: Sent " + messageCount + " messages in " + (endTime - startTime) + " ms");

        executor.shutdown();


    }


    public  static void produceObjectMessage(Random random, RestTemplate restTemplate, String url){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Cookie", "JSESSIONID=3C65349D5878D1997AD7DFE65DC2275D");

        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setAccountId(120343);
        kafkaMessage.setPayloadType("PRODUCT");

        ModelProperties modelProperties = new ModelProperties();
        Product customEntity = new Product();
        customEntity.setProductName("Product" + random.nextInt(1000));
        customEntity.setProductAddress("Address" + random.nextInt(1000));
        customEntity.setProductCode("Code" + random.nextInt(100000));
        customEntity.setPrice(random.nextInt(10000));
        customEntity.setProductCategory("Category" + random.nextInt(1000));
        // customEntity.setType("product");
        modelProperties.setCustomEntity(customEntity);

        kafkaMessage.setModelProperties(modelProperties);

        SystemProperties systemProperties = new SystemProperties();
        systemProperties.setSystemId("SystemId" + random.nextInt(1000));
        systemProperties.setSystemName("SystemName" + random.nextInt(1000));
        kafkaMessage.setSystemProperties(systemProperties);

        HttpEntity<KafkaMessage> entity = new HttpEntity<>(kafkaMessage, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        System.out.println("Produced a message to Kafka: " + response.getStatusCode());
    }

}
