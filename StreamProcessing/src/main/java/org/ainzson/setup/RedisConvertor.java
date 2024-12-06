package org.ainzson.setup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class RedisConvertor {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String,Object> convertToMapOfObjects(Map<String,String> shift) {
        Map<String,Object> shiftInfo = new HashMap<>();

        for(Map.Entry<String,String> entry: shift.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Object convertedValue = convertStringToObject(value);
            shiftInfo.put(key, convertedValue);
        }

        return shiftInfo;
    }

    private static Object convertStringToObject(String value)  {
        if (value == null) {
            return null;
        }
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(value);
        }

        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e) {
            // Not an integer
//            log.warn("Not an integer");
        }

        try {
            return Double.parseDouble(value);
        }
        catch (NumberFormatException e) {
//            log.warn("Not a double");
        }

        try {
            return ZonedDateTime.parse(value, DateTimeFormatter.ISO_DATE);
        }
        catch (Exception e) {
//            log.warn("Not a correct time");
        }
        try {
            if (value.startsWith("[") && value.endsWith("]")) {
                return objectMapper.readValue(value, List.class);
            }
            else if (value.startsWith("{") && value.endsWith("}")) {
                return objectMapper.readValue(value,Map.class);
            }
        }
        catch (JsonProcessingException ex) {
            log.error("Issue when deserializing shift data");
        }
        return value;
    }
}
