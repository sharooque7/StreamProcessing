package org.ainzson.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static String serializeToString(Object object) throws Exception {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            log.error("Error processing shift Serialization: {}", exception.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static <T> T deserializeFromString(String data, Class<T> clazz) throws Exception {
        try {
            return objectMapper.readValue(data, clazz);
        }
        catch (JsonProcessingException exception) {
            log.error("Error processing shift Deserialization: {}", exception.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;

    }
}
