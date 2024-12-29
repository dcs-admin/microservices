package com.anji.kafka.entity;

//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
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
public class Customer implements  CustomEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long customerId;
    private String customerName;
    private String customerAddress;
    private String customerCode;
    private int rating;
    private String customerCategory; //HAIR, BEATY, FERMI, GENERAL, MEDICAL, MISC
    private String type;

}
