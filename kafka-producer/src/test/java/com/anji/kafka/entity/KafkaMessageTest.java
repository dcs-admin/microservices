package com.anji.kafka.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.kafka.shared.entity.Customer;
import org.kafka.shared.entity.KafkaMessage;
import org.kafka.shared.entity.Product;

import static org.junit.jupiter.api.Assertions.*;

public class KafkaMessageTest {

    @Test
    public void testSerializationWithProduct() throws Exception {
        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.getModelProperties().setCustomEntity(new Product(123, "Test Product","MahaRastra", "Test", 123, "Test"));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(kafkaMessage);
        System.out.println(jsonString);
        assertNotNull(jsonString);
        assertTrue(jsonString.contains("\"type\":\"product\""));
        assertTrue(jsonString.contains("\"productId\":123"));
        assertTrue(jsonString.contains("\"productName\":\"Test Product\""));
    }

    @Test
    public void testSerializationWithCustomer() throws Exception {
        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.getModelProperties().setCustomEntity(new Customer());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(kafkaMessage);

        assertNotNull(jsonString);
        assertTrue(jsonString.contains("\"type\":\"customer\""));
//        assertTrue(jsonString.contains("\"customerId\":\"cust123\""));
//        assertTrue(jsonString.contains("\"customerName\":\"Test Customer\""));
    }


    @Test
    public void testDeserializationWithProduct() throws Exception {
        String jsonString = "{\"modelProperties\":{\"customEntity\":{\"type\":\"product\",\"productId\":123,\"productMessage\":123,\"productName\":\"Test Product\",\"productAddress\":\"MahaRastra\",\"productCode\":\"Test\",\"price\":123,\"productCategory\":\"Test\"}}}";

        ObjectMapper objectMapper = new ObjectMapper();
        KafkaMessage kafkaMessage = objectMapper.readValue(jsonString, KafkaMessage.class);

        assertNotNull(kafkaMessage);
        assertNotNull(kafkaMessage.getModelProperties().getCustomEntity());
        assertTrue(kafkaMessage.getModelProperties().getCustomEntity() instanceof Product);
        Product product = (Product) kafkaMessage.getModelProperties().getCustomEntity();
        assertEquals(123, product.getProductId());
        assertEquals("Test Product", product.getProductName());
    }

    @Test
    public void testDeserializationWithCustomer() throws Exception {
        String jsonString = "{\"modelProperties\":{\"customEntity\":{\"type\":\"customer\",\"customerId\":123,\"customerName\":\"Test Customer\"}}}";

        ObjectMapper objectMapper = new ObjectMapper();
        KafkaMessage kafkaMessage = objectMapper.readValue(jsonString, KafkaMessage.class);

        assertNotNull(kafkaMessage);
        assertNotNull(kafkaMessage.getModelProperties().getCustomEntity());
        assertTrue(kafkaMessage.getModelProperties().getCustomEntity() instanceof Customer);
        Customer customer = (Customer) kafkaMessage.getModelProperties().getCustomEntity();
        assertEquals(123, customer.getCustomerId());
        assertEquals("Test Customer", customer.getCustomerName());
    }
}