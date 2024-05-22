package com.anji.approvals.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long productId;
    private String productName;
    private String productAddress;
    private String productCode;
    private int price;
    private String category; //HAIR, BEATY, FERMI, GENERAL, MEDICAL, MISC

}
