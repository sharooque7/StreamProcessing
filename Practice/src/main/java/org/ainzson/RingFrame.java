package org.ainzson;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Generated;


@Setter
@Getter
public class RingFrame {
    // other fields...

    @JsonDeserialize(using = EpochToIso8601Deserializer.class)
    @JsonProperty("shiftdate")
    private String shiftDate;
    @JsonDeserialize(using = EpochToIso8601Deserializer.class)
    private String ts;
    private String name;
    private Double points;



    // getters and setters...
}
