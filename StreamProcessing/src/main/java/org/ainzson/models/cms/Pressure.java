package org.ainzson.models.cms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.ainzson.utils.EpochToISO8601Deserializer;

@Getter
@Setter
@Data
@Builder(builderMethodName = "PressureBuilder")
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
    private int count = 1;

    public Pressure calculate(Pressure pressure) {
        this.ts = pressure.getTs();
        this.deviceId = pressure.getDeviceId();
        this.tags  = pressure.getTags();

        this.pressure += pressure.getPressure() / count;
        this.absolute_pressure += pressure.getAbsolute_pressure() / count;
        this.differential_pressure += pressure.getDifferential_pressure() / count;
        this.temperature += pressure.getTemperature() / count;
        this.count++;

        return this;
    }
}