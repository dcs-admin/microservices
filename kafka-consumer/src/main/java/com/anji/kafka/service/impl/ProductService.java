package com.anji.kafka.service.impl;

import org.kafka.shared.entity.CustomEntity;
import org.kafka.shared.entity.KafkaMessage;

import org.kafka.shared.entity.Product;
import com.anji.kafka.repository.ProductRepository;
import com.anji.kafka.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ProductService implements GenericService {

    @Autowired
    private ProductRepository productRepository;
    @Override
    public CustomEntity process(KafkaMessage message) {
        Product product = message.getModelProperties().getProduct();
        if(product != null){
            productRepository.save(product);
        }else{
            System.out.println("No Product found in the desirilised  message");
        }
        return product;
    }
}
