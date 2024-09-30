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
public class Vibration {
    @JsonProperty("tags")
    private Tags  tags;
    @JsonProperty("ts")
    @JsonDeserialize(using = EpochToISO8601Deserializer.class)
    private String ts;
    @JsonProperty("deviceid")
    private String deviceId;
    @JsonProperty("acceleration")
    private double acceleration;
    @JsonProperty("velocity")
    private double velocity;
    @JsonProperty("displacement")
    private double displacement;
    @JsonProperty("frequency")
    private double frequency;
}
