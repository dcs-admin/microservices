package com.anji.kafkademo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;



// @Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
// @Table(name = "userr")
public class Customer {
 
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;

}
