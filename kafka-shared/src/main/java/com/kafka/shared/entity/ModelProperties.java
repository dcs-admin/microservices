package com.kafka.shared.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ModelProperties implements java.io.Serializable {
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
    private String modelPublishedVersion;
    private String modelPublishedStatus;
    private String modelPublishedDescription;
    private String modelPublishedDateFrom;
    private String modelPublishedDateTo;
    private String modelPublishedByFrom;
    private String modelPublishedByTo;
    private String modelPublishedVersionFrom;
    private String modelPublishedVersionTo;
    private String modelPublishedStatusFrom;
    private String modelPublishedStatusTo;
    private String modelPublishedDescriptionFrom;
    private String modelPublishedDescriptionTo;
    private String modelPublishedDateFromTo;
    private String modelPublishedByFromTo;
    private String modelPublishedVersionFromTo;
    private String modelPublishedStatusFromTo;
    private String modelPublishedDescriptionFromTo;
    private String modelPublishedDateFromToFrom;
    private String modelPublishedByFromToFrom;
    private String modelPublishedVersionFromToFrom;
    private String modelPublishedStatusFromToFrom;
    private String modelPublishedDescriptionFromToFrom;
    private String modelPublishedDateFromToTo;
    private String modelPublishedByFromToTo;
    private String modelPublishedVersionFromToTo;
    private String modelPublishedStatusFromToTo;
    private String modelPublishedDescriptionFromToTo;
    private String modelPublishedDateFromFrom;
    private String modelPublishedByFromFrom;
    private String modelPublishedVersionFromFrom;
    private String modelPublishedStatusFromFrom;
    private String modelPublishedDescriptionFromFrom;
    private String modelPublishedDateFromToFromTo;
    private String modelPublishedByFromToFromTo;
    private String modelPublishedVersionFromToFromTo;
    private String modelPublishedStatusFromToFromTo;
    private String modelPublishedDescriptionFromToFromTo;
    private String modelPublishedDateFromToToFrom;
    private String modelPublishedByFromToToFrom;
    private String modelPublishedVersionFromToToFrom;
    private String modelPublishedStatusFromToToFrom;
    private String modelPublishedDescriptionFromToToFrom;
    private String modelPublishedDateFromFromTo;
    private String modelPublishedByFromFromTo;
    private String model;

//    private Product product = new Product();
//    private Customer customer = new Customer();

    private CustomEntity customEntity;

    public Product getProduct() {
        if(customEntity instanceof  Product){
            return (Product) customEntity;
        }
        return null;
    }

    public Customer getCustomer() {
        if(customEntity instanceof  Customer){
            return (Customer) customEntity;
        }
        return null;
    }
}
