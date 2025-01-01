package org.kafka.shared.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Use a name to identify the type
        include = JsonTypeInfo.As.PROPERTY, // Include it as a property in the JSON
        property = "type" // The property name that will hold the type information
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Product.class, name = "product"),
        @JsonSubTypes.Type(value = Customer.class, name = "customer")
})
public interface CustomEntity  {
}
