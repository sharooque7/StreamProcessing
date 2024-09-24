package org.ainzson.convertors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ainzson.schema.RingFrame40StreamDTO;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

public class RingFrame40Serde extends Serdes.WrapperSerde<RingFrame40StreamDTO> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public RingFrame40Serde() {
        super(new RingFrame40Serde.Ringframe40Serializer(), new RingFrame40Serde.Ringframe40Deserializer());
    }
    public static class Ringframe40Serializer implements Serializer<RingFrame40StreamDTO> {
        @Override
        public byte[] serialize(String topic, RingFrame40StreamDTO data) {
            try {
                return objectMapper.writer().writeValueAsBytes(data);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Ringframe40Deserializer implements Deserializer<RingFrame40StreamDTO> {
        @Override
        public RingFrame40StreamDTO deserialize(String topic, byte[] data) {
            try {
                return objectMapper.readValue(new String(data), RingFrame40StreamDTO.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
