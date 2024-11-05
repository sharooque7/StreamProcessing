package org.ainzson.Serdes;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonDeserialization<T> implements Deserializer<T> {
    private final Class<T> deserializeClass;
    private  final static Logger logger = LoggerFactory.getLogger(JsonDeserialization.class);

    public JsonDeserialization(Class<T> deserializeClass) {
        this.deserializeClass = deserializeClass;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return JsonSerialization.MAPPER.readValue(data,deserializeClass);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}
