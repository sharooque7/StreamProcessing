package org.ainzson.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ainzson.schema.Temperature;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TemperatureSerde extends Serdes.WrapperSerde<Temperature> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public TemperatureSerde() {
        super(new TemperatureSerializer(), new TemperatureDeserializer());
    }

    public static class TemperatureSerializer implements Serializer<Temperature> {
        @Override
        public byte[] serialize(String topic, Temperature data) {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error serializing Temperature data", e);
            }
        }

        @Override
        public void close() {
            // Close any resources if needed
        }
    }

    public static class TemperatureDeserializer implements Deserializer<Temperature> {
        @Override
        public Temperature deserialize(String topic, byte[] data) {
            try {
                return objectMapper.readValue(data, Temperature.class);
            } catch (IOException e) {
                throw new RuntimeException("Error deserializing Temperature data", e);
            }
        }

        @Override
        public void close() {
            // Close any resources if needed
        }
    }
}
