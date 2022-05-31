package com.anji.order.service;

import com.anji.order.entity.Order;
import com.anji.order.repository.OrderRepository;
import com.anji.order.warpper.Customer;
import com.anji.order.warpper.OrderWrapper;
import com.anji.order.warpper.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class OrderService { 
    @Autowired
    private RestTemplate restTemplate; 

    @Autowired
    private OrderRepository orderRepository;

    public Order saveOrder(Order order){
       return orderRepository.save(order);
    }


    public OrderWrapper findOrderWithCompleteInfo(long orderId) throws Exception {

        log.info("In::findOrderWithCompleteInfo::"+orderId);
        OrderWrapper orderWrapper = new OrderWrapper();
        Order order = orderRepository.findByOrderId(orderId);
        if(order != null) {
            Customer customer =
                    this.restTemplate.getForObject("http://CUSTOMER-SERVICE/customers/" + order.getCustomerId(),
                            Customer.class);
            Product product =
                    this.restTemplate.getForObject("http://PRODUCT-SERVICE/products/" + order.getProductId(),
                            Product.class);
            orderWrapper.setCustomer(customer);
            orderWrapper.setProduct(product);
            orderWrapper.setOrder(order);
        }else{
            orderWrapper.setResponseCode(500);
            orderWrapper.setResponseMessage("OrderId:"+orderId+" not found from database");
        }

        log.info("Out::"+orderWrapper);
        return orderWrapper;
    }

}

