package com.product_agent.global.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.LinkedHashSet;
import java.util.List;

public interface JsonUtil {

    <T> T parseJson(String json, Class<T> valueType);

    <T> List<T> parseJsonToList(String json, Class<T> valueType);

    <T> LinkedHashSet<T> parseJsonToLinkedHashSet(String json, Class<T> valueType);

    String toJsonString(Object obj);

    ObjectNode toObjectNode(Object obj);
}