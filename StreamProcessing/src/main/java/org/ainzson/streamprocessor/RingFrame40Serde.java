package org.ainzson.streamprocessor;

import com.google.gson.Gson;
import org.ainzson.schema.RingFrame40StreamDTO;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

public class RingFrame40Serde extends Serdes.WrapperSerde<RingFrame40StreamDTO> {

    private  static  final Gson gson = new Gson();

    public RingFrame40Serde() {
        super(new RingFrame40Serde.Ringframe40Serializer(), new RingFrame40Serde.Ringframe40Deserializer());
    }
    public static class Ringframe40Serializer implements Serializer<RingFrame40StreamDTO> {
        @Override
        public byte[] serialize(String topic, RingFrame40StreamDTO data) {
            return gson.toJson(data).getBytes();
        }
    }

    public static class Ringframe40Deserializer implements Deserializer<RingFrame40StreamDTO> {
        @Override
        public RingFrame40StreamDTO deserialize(String topic, byte[] data) {
            return gson.fromJson(new String(data), RingFrame40StreamDTO.class);
        }
    }

}
