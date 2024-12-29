package com.anji.kafka.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements CustomEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long productId;
    private String productName;
    private String productAddress;
    private String productCode;
    private int price;
    private String productCategory; //HAIR, BEATY, FERMI, GENERAL, MEDICAL, MISC
    private String type;

}
