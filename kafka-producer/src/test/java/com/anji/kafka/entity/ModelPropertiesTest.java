package com.anji.kafka.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.kafka.shared.entity.ModelProperties;

import static org.junit.jupiter.api.Assertions.*;

public class ModelPropertiesTest {

    @Test
    public void testSerialization() throws Exception {
        ModelProperties modelProperties = new ModelProperties();
        modelProperties.setModelId("123");
        modelProperties.setModelName("Test Model");


        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(modelProperties);

        assertNotNull(jsonString);
        assertTrue(jsonString.contains("\"modelId\":\"123\""));
        assertTrue(jsonString.contains("\"modelName\":\"Test Model\""));
    }

    @Test
    public void testDeserialization() throws Exception {
        String jsonString = "{\"modelId\":\"123\",\"modelName\":\"Test Model\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        ModelProperties modelProperties = objectMapper.readValue(jsonString, ModelProperties.class);

        assertNotNull(modelProperties);
        assertEquals("123", modelProperties.getModelId());
        assertEquals("Test Model", modelProperties.getModelName());
    }
}