package org.ainzson.Sensors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Timestamp;
import java.util.Random;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pressure {
    @JsonProperty("ts")
    private String ts;
    @JsonProperty("deviceid")
    private String deviceId;
    @JsonProperty("pressure")
    private double pressure;
    @JsonProperty("absolute_pressure")
    private double absolute_pressure;
    @JsonProperty("differential_pressure")
    private double differential_pressure;
    @JsonProperty("temperature")
    private double temperature;
    @JsonProperty("tags")
    private Tags tags;
    public Pressure(String assetId, String department,String deviceId) {
        this.setTags(new Tags(assetId,department));
        this.setDeviceId(deviceId);
    }

    public void generateRandomData() {
        Random random = new Random();
        this.ts = String.valueOf(new Timestamp(System.currentTimeMillis()));

        // Temperature-related values
        // Pressure-related values
        this.pressure = random.nextDouble() * 100;       // General pressure (0 to 100 bar)
        this.absolute_pressure = random.nextDouble() * 200; // Absolute pressure (0 to 200 bar)
        this.differential_pressure = random.nextDouble() * 10; // Differential pressure (0 to 10 bar)

        // Temperature at which pressure is measured
        this.temperature = -50 + (random.nextDouble() * 200); // Temperature range from -50 to 150°C
    }
}