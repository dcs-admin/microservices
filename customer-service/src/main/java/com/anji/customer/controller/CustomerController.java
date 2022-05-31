package com.anji.customer.controller;

import com.anji.customer.entity.Customer;
import com.anji.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @PostMapping("/")
    public Customer saveCustomer(@RequestBody Customer customer){
        log.info("Post:saveCustomer:"+customer);
        return this.customerService.saveCustomer(customer);
    }


    @GetMapping("/{customerId}")
    public Customer getCustomer(@PathVariable long customerId) throws Exception {
        log.info("Get:findCustomerId:"+customerId);
        return this.customerService.getCustomer(customerId);

    }

}
