package com.anji.customer.service;

import com.anji.customer.entity.Customer;
import com.anji.customer.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    public Customer saveCustomer(Customer customer){
       return customerRepository.save(customer);
    }

    public Customer getCustomer(Long customerId){
        return customerRepository.findByCustomerId(customerId);
    }


}

