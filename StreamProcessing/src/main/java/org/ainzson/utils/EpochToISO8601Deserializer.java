package org.ainzson.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class EpochToISO8601Deserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        if (p.getCurrentToken().isNumeric()) {
            long epochTime = p.getLongValue();
            Instant instant = Instant.ofEpochMilli(epochTime);
            return DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC")).format(instant);
        } else {
            // If not a long, return the string value as-is
            return p.getText();
        }
    }
}

