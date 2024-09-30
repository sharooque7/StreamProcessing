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
public class Humidity {
    @JsonProperty("ts")
    @JsonDeserialize(using = EpochToISO8601Deserializer.class)
    private String ts;
    @JsonProperty("deviceid")
    private String deviceId;
    @JsonProperty("relative_humidity")
    private double relative_humidity;
    @JsonProperty("absolute_humidity")
    private double absolute_humidity;
    @JsonProperty("dew_point")
    private double dew_point;
    @JsonProperty("humidity_ratio")
    private double humidity_ratio;
    @JsonProperty("tags")
    private Tags tags;
}


