package org.ainzson.Serdes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.ainzson.utils.Mapper;
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class JsonSerialization<T> implements Serializer<T> {
    final static ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            return MAPPER.writeValueAsBytes(data);
        }
        catch (JsonProcessingException e) {
            return new byte[0];
        }
    }
}
