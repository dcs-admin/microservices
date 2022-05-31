package com.anji.order.controller;

import com.anji.order.entity.Order;
import com.anji.order.service.OrderService;
import com.anji.order.warpper.OrderWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/")
    public Order saveOrder(@RequestBody Order order){
        log.info("Post:saveOrder:"+order);
        return this.orderService.saveOrder(order);
    }


    @GetMapping("/{orderId}")
    public OrderWrapper findOrderWithCompleteInfo(@PathVariable long orderId) throws Exception {
        log.info("Get:findOrderWithCompleteInfo:"+orderId);
        return this.orderService.findOrderWithCompleteInfo(orderId);

    }

}
