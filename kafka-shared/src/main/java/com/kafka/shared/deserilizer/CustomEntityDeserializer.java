package com.kafka.shared.deserilizer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.kafka.shared.entity.CustomEntity;
import com.kafka.shared.entity.Product;

import java.io.IOException;

public class CustomEntityDeserializer extends StdDeserializer<CustomEntity> {

    public CustomEntityDeserializer() {
        super(CustomEntity.class);
    }

    @Override
    public CustomEntity deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        JsonNode node = mapper.readTree(parser);

        // Check for specific fields to determine the type
//        if (node.has("productId") && node.has("productName")) {
//            return mapper.treeToValue(node, Product.class);
//        } else if (node.has("customerId") && node.has("customerName")) {
//            return mapper.treeToValue(node, Customer.class);
//        } else {
//            throw new IllegalArgumentException("Unable to determine the type of CustomEntity");
//        }

        return new Product();
    }
}
