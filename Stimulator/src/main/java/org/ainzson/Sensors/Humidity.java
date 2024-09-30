package org.ainzson.Sensors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Humidity implements Runnable {
    @JsonProperty("ts")
    private String ts;
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

    @Override
    public void run() {

    }
}


