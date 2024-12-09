package org.ainzson.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
public class EpochToISO8601Deserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        if (p.getCurrentToken().isNumeric()) {
            long epochTime = p.getLongValue();
            return Instant.ofEpochMilli(epochTime);
//            return Instant.parse(DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC")).format(instant));
        } else {
            // If not a long, return the string value as-is
            return Instant.parse(p.getText());
        }
    }
}

