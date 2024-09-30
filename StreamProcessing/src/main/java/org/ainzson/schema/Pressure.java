package org.ainzson.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.ainzson.utils.EpochToISO8601Deserializer;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pressure {
    @JsonProperty("ts")
    @JsonDeserialize(using = EpochToISO8601Deserializer.class)
    private String ts;
    @JsonProperty("pressure")
    private double pressure;
    @JsonProperty("deviceid")
    private String deviceId;
    @JsonProperty("absolute_pressure")
    private double absolute_pressure;
    @JsonProperty("differential_pressure")
    private double differential_pressure;
    @JsonProperty("temperature")
    private double temperature;
    @JsonProperty("tags")
    private Tags tags;
}