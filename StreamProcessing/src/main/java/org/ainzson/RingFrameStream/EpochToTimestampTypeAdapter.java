package org.ainzson.RingFrameStream;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class EpochToTimestampTypeAdapter extends TypeAdapter<Timestamp> {
    // Formatter for the desired output format
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
    // Fixed timezone (e.g., UTC)
    private static final ZoneId targetZone = ZoneId.of("UTC");

    @Override
    public void write(JsonWriter out, Timestamp value) throws IOException {
        if (value != null) {
            // Convert Timestamp to formatted string
            String formattedTimestamp = formatter.format(value.toLocalDateTime());
            out.value(formattedTimestamp);  // Write the formatted string to JSON
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
            System.out.println("Converted epoch: " + epochMilli + " to timestamp: " + dateTime.format(formatter));
            return Timestamp.valueOf(dateTime.format(formatter));  // Convert to Timestamp
        }
        return null;  // Handle null values
    }
}