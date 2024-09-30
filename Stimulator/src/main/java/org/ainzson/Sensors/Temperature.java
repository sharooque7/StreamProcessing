package org.ainzson.Sensors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Temperature {
    @JsonProperty("ts")
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


    public Temperature(String assetId, String department,String deviceId) {
        this.setTags(new Tags(assetId,department));
        this.setDeviceId(deviceId);
    }

    public void generateRandomDate() {
        Random random = new Random();
        this.ts = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.dew_point = random.nextDouble() * 10; // Dew point between 0 and 10
        this.temp = random.nextDouble() * 40;      // Temperature between 0 and 40
        this.heat_index = this.temp + (random.nextDouble() * 5); // Heat index based on temp
        this.humidity = random.nextDouble() * 100; // Hum
    }
}