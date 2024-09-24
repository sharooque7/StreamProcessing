package org.ainzson.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.ainzson.schema.RingFrame40StreamDTO;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class RingFrameTypeAdapter extends TypeAdapter<RingFrame40StreamDTO> {

    @Override
    public void write(final JsonWriter out, final RingFrame40StreamDTO ringFrame40StreamDTO) throws IOException {
        out.beginObject();
        out.name("ts").value(ringFrame40StreamDTO.getTs());
        out.endObject();
    }

    @Override
    public RingFrame40StreamDTO read(final JsonReader in) throws IOException {
        final RingFrame40StreamDTO ringFrame40StreamDTO = new RingFrame40StreamDTO();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "ts":
                    String ts = epochToISO8061(Long.parseLong(ringFrame40StreamDTO.getTs()));
                    ringFrame40StreamDTO.setTs(ts);
                    break;
            }
        }
        in.endObject();

        return ringFrame40StreamDTO;
    }
    public String epochToISO8061(long epochMillis) {
        Instant instant = Instant.ofEpochMilli(epochMillis);
        return DateTimeFormatter.ISO_INSTANT.format(instant);
    }

}
