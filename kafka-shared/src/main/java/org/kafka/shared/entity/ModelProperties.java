package org.kafka.shared.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelProperties implements Serializable {
    private String modelId;
    private String modelName;
    private String modelVersion;
    private String modelType;
    private String modelCategory;
    private String modelDescription;
    private String modelStatus;
    private String modelOwner;
    private String modelCreatedDate;
    private String modelModifiedDate;
    private String modelPublishedDate;
    private String modelPublishedBy;

//    private Product product = new Product();
//    private Customer customer = new Customer();

    private CustomEntity customEntity;

    public Product getProduct() {
        if(customEntity != null && customEntity instanceof  Product){
            return (Product) customEntity;
        }
//        else if(product != null){
//            return product;
//        }
        return null;
    }

    public Customer getCustomer() {
        if(customEntity != null && customEntity instanceof  Customer){
            return (Customer) customEntity;
        }
//        else  if(customer != null){
//            return customer;
//        }
        return null;
    }
}
