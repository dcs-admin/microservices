package com.anji.kafka.service.impl;

import org.kafka.shared.entity.CustomEntity;
import org.kafka.shared.entity.Customer;


import org.kafka.shared.entity.KafkaMessage;
import com.anji.kafka.repository.CustomerRepository;
import com.anji.kafka.repository.ProductRepository;
import com.anji.kafka.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements GenericService {
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public CustomEntity process(KafkaMessage message) {
        Customer customer = message.getModelProperties().getCustomer();
        if(customer != null){
            customerRepository.save(customer);
        }else{
            System.out.println("No Customer found in the desirilised message");
        }
        return customer;
    }
}
