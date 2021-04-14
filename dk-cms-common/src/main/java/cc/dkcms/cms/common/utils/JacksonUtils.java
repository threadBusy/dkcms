package cc.dkcms.cms.common.utils;

import cc.dkcms.cms.common.define.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();



    public static <T> T readValue(String jsonStr, Class<T> t) {
        try {
            return objectMapper.readValue(jsonStr, t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T readValue(String jsonStr, TypeReference<T> t) {
        try {
            return objectMapper.readValue(jsonStr, t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Result writeValueAsString(Object object) {
        try {
            return Result.success(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            return Result.fail(e.getMessage());
        }
    }
}