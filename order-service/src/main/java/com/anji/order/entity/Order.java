package com.anji.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivery_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    private long customerId;
    private long productId;
    private Date orderDate = new Date();
    private String location;
    private Date expectedDelivery = new Date(6);
    private int rating;

}
