package org.ainzson.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class EpochToTimestampTypeAdapter extends TypeAdapter<Timestamp> {
    // Fixed timezone (e.g., UTC)
    private static final ZoneId targetZone = ZoneId.of("UTC");

    @Override
    public void write(JsonWriter out, Timestamp value) throws IOException {
        if (value != null) {
            // Convert Timestamp to epoch milliseconds
            long epochMilli = value.toInstant().toEpochMilli();
            out.value(epochMilli);  // Write the epoch milliseconds to JSON
        } else {
            out.nullValue();  // Handle null values
        }
    }

    @Override
    public Timestamp read(JsonReader in) throws IOException {
        if (in != null && in.peek() != null) {
            // Read the epoch milliseconds from JSON
            long epochMilli = in.nextLong();
            // Convert to LocalDateTime in the specified target timezone (e.g., UTC)
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), targetZone);
            return Timestamp.valueOf(dateTime);  // Convert to Timestamp
        }
        return null;  // Handle null values
    }
}
