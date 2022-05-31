package com.anji.order.warpper;

import com.anji.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderWrapper {

    private Order order;
    private Product product;
    private Customer customer;

    private int responseCode = 200 ;
    private String responseMessage;

}
