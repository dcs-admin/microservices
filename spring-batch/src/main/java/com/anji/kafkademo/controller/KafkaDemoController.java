package com.anji.kafkademo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.anji.kafkademo.model.Customer;
import com.anji.kafkademo.service.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/push") 
public class KafkaDemoController {

    @Autowired
    private KafkaProducer kafkaProducer; 

    @PostMapping("/")
    public String saveCustomer(@RequestBody Customer customer) throws JsonProcessingException{
        
        ObjectMapper objectMapper = new ObjectMapper();
        String customerJson = objectMapper.writeValueAsString(customer);
        System.out.println("customerJson: as string: "+customerJson);

        // // Use this JSON string in Kafka
        // kafkaTemplate.send("topicName", customerJson);

         this.kafkaProducer.sendMessage(customerJson);
         return null;
    }


    // @GetMapping("/{customerId}")
    // public Customer getCustomer(@PathVariable long customerId) throws Exception {
    //     log.info("Get:findCustomerId:"+customerId);
    //     return this.customerService.getCustomer(customerId);

    // }

}
