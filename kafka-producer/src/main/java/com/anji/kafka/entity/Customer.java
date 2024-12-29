package com.anji.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements  CustomEntity, Serializable {

    private long customerId;
    private String customerName;
    private String customerAddress;
    private String customerCode;
    private int rating;
    private String customerCategory; //HAIR, BEATY, FERMI, GENERAL, MEDICAL, MISC

}