package com.anji.kafka.service.factory;

import com.anji.kafka.service.GenericService;
import com.anji.kafka.service.impl.CustomerService;
import com.anji.kafka.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceFactory {

    @Autowired
    private  ProductService productService;

    @Autowired
    private  CustomerService customerService;


    public  GenericService getService(String serviceType) {
        if (serviceType == null) {
            return null;
        }
        if (serviceType.equalsIgnoreCase("PRODUCT")) {
            return productService;
        } else if (serviceType.equalsIgnoreCase("CUSTOMER")) {
            return customerService;
        } else{
            System.out.println("Invalid PayloadType: GenericService not found");
        }
        return null;
    }
}
