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
public class Vibration {
    @JsonProperty("tags")
    private Tags  tags;
    @JsonProperty("ts")
    private String ts;
    @JsonProperty("deviceid")
    private String deviceid;
    @JsonProperty("acceleration")
    private double acceleration;
    @JsonProperty("velocity")
    private double velocity;
    @JsonProperty("displacement")
    private double displacement;
    @JsonProperty("frequency")
    private double frequency;

    public Vibration(String assetId, String department,String deviceid) {
        this.tags = Tags.builder().assetId(assetId).departmentId(department).build();
        this.setDeviceid(deviceid);
    }

    public void generateRandomData() {
        Random random = new Random();
        // Assign timestamp as the current time
        this.ts = String.valueOf(new Timestamp(System.currentTimeMillis()));
        // Generating appropriate ranges for vibration parameters:
        this.acceleration = roundToTwoDecimalPlaces(random.nextDouble() * 20); // Acceleration in g, range: 0 - 20 g
        this.velocity = roundToTwoDecimalPlaces(random.nextDouble() * 100);    // Velocity in mm/s, range: 0 - 100 mm/s
        this.displacement = roundToTwoDecimalPlaces(random.nextDouble() * 500); // Displacement in micrometers, range: 0 - 500 µm
        this.frequency = roundToTwoDecimalPlaces(random.nextDouble() * 5000);  // Frequency in Hz, range: 0 - 5000 Hz
    }

    // Helper function to round values to 2 decimal places for better readability
    private double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}
