package cc.dkcms.cms.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String writeValueAsString(Object o) {
        if (o == null) {
            return "null";
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "null";
    }

    public static Map<String, String> readValue(String extJson, TypeReference<Map<String, String>> mapTypeReference) {
        try {
            return OBJECT_MAPPER.readValue(extJson, new TypeReference<Map<String, String>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private static String kvSp    = "{@}";
    private static String fieldSp = "[@]";

    public static String mySonEncode(Map<String, Object> map) {

        LinkedList<String> json = new LinkedList<>();
        for (String key : map.keySet()) {
            String value = String.valueOf(map.get(key));
            value = value.replace("'", "\'");
            json.add(key + kvSp + value);
        }
        return StringUtils.join(json, fieldSp);
    }

    public static Map<String, String> mySonDecode(String jsonString) {
        Map<String, String> ret  = new HashMap<>();
        String[]            strs = StringUtils.splitByWholeSeparator(jsonString, fieldSp);
        if (strs == null || strs.length == 0) {
            return ret;
        }
        for (String s : strs) {
            int pos = s.indexOf(kvSp);
            ret.put(s.substring(0, pos), s.substring(pos + kvSp.length()));
        }
        return ret;

    }
}
