package com.wuyiccc.hellomq.broker.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.Objects;

/**
 * @author wuyiccc
 * @date 2020/10/25 7:23
 * json工具类
 */
public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.registerModules(new JavaTimeModule()); // 解决 LocalDateTime 的序列化
    }

    /**
     * 对象转json字符串
     *
     * @param data 对象
     * @return json字符串
     */
    public static String objectToJson(Object data) {

        return objectToJson(data, false);
    }

    public static String objectToJson(Object data, boolean format) {

        if (format) {
            try {
                return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                return MAPPER.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * json字符串转对象
     *
     * @param jsonData json字符串
     * @param beanType 对象类型
     * @return 对象
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转List
     *
     * @param jsonData json字符串对象
     * @param beanType 对象类型
     * @return 对象
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转json对象
     *
     * @param jsonData json字符串
     * @return json对象
     */
    public static JsonNode jsonToJsonNode(String jsonData) {

        try {
            return MAPPER.readTree(jsonData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JsonNode对象转
     *
     * @param jsonNode jsonNode对象
     * @param jsonType class对象
     * @return 对象
     */
    public static <T> T jsonNodeToPojo(JsonNode jsonNode, Class<T> jsonType) {

        try {
            return MAPPER.treeToValue(jsonNode, jsonType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
