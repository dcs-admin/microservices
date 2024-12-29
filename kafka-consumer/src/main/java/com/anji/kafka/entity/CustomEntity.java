package com.anji.kafka.entity;

import com.anji.kafka.deserializer.CustomEntityDeserializer;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

//@JsonDeserialize(using = CustomEntityDeserializer.class)
//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.NAME, // Use a name to identify the type
//        include = JsonTypeInfo.As.PROPERTY, // Include it as a property in the JSON
//        property = "type" // The property name that will hold the type information
//)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = Product.class, name = "product"),
//        @JsonSubTypes.Type(value = Customer.class, name = "customer")
//})
public interface CustomEntity {
}
