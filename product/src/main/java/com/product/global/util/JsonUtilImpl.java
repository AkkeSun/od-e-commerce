package com.product.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.LinkedHashSet;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class JsonUtilImpl implements JsonUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T parseJson(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> parseJsonToList(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> LinkedHashSet<T> parseJsonToLinkedHashSet(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory()
                .constructCollectionType(LinkedHashSet.class, valueType));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObjectNode toObjectNode(Object obj) {
        return objectMapper.valueToTree(obj);
    }
}