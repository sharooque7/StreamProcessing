package org.ainzson.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Temperature {
    @JsonProperty("ts")
    @JsonDeserialize(using = EpochToISO8601Deserializer.class)
    private String ts;
    @JsonProperty("deviceid")
    private String deviceId;
    @JsonProperty("temp")
    private double temp;
    @JsonProperty("humidity")
    private double humidity;
    @JsonProperty("dew_point")
    private double dew_point;
    @JsonProperty("heat_index")
    private double heat_index;
    @JsonProperty("tags")
    private Tags tags;
    private int count = 1;

    public Temperature calculate(Temperature temperature) {
        this.ts = temperature.getTs();
        this.deviceId = temperature.getDeviceId();
        this.tags  = temperature.getTags();

        this.temp += temperature.getTemp() / count;
        this.heat_index += temperature.getHeat_index() / count;
        this.dew_point += temperature.getDew_point() / count;
        this.humidity += temperature.getHumidity() / count;
        this.count++;

        return this;
    }
}
