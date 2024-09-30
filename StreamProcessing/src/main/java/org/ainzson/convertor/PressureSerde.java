package org.ainzson.convertor;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.ainzson.schema.Pressure;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

public class PressureSerde extends Serdes.WrapperSerde<Pressure> {

    private static final Gson gson = new Gson();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public PressureSerde() {
        super(new PressureSerde.PressureSerializer(), new PressureSerde.PressureDerializer());
    }
    public static class PressureSerializer implements Serializer<Pressure> {
        @Override
        public byte[] serialize(String topic, Pressure data) {
            try {
                return objectMapper.writer().writeValueAsBytes(data);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class PressureDerializer implements Deserializer<Pressure> {
        @Override
        public Pressure deserialize(String topic, byte[] data) {
            try {
                return objectMapper.readValue(new String(data), Pressure.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}



