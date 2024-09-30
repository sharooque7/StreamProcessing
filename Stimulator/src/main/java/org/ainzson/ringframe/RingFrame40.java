package org.ainzson.ringframe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Timestamp;
import java.util.Random;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RingFrame40 extends Tags{

    // Timestamp fields
    @JsonProperty("ts")
    private String ts;
    @JsonProperty("ts_act")
    private String tsAct;

    // Shift-related fields
    @JsonProperty("shift_number")
    private float shiftNumber;
    @JsonProperty("shift_date")
    private float shiftDate;
    @JsonProperty("shift_month")
    private float shiftMonth;
    @JsonProperty("shift_year")
    private float shiftYear;
    @JsonProperty("shift_hour")
    private float shiftHour;
    @JsonProperty("shift_minute")
    private float shiftMinute;

    // Production fields
    @JsonProperty("production_in_hanks")
    private float productionInHanks;
    @JsonProperty("production_in_meters")
    private float productionInMeters;
    @JsonProperty("weight_per_spindle")
    private float weightPerSpindle;
    @JsonProperty("no_of_doffs")
    private float noOfDoffs;
    @JsonProperty("air_consumption")
    private float airConsumption;


    // Runtime fields
    @JsonProperty("run_time_hour")
    private float runTimeHour;
    @JsonProperty("run_time_minute")
    private float runTimeMinute;
    @JsonProperty("idle_time_hour")
    private float idleTimeHour;
    @JsonProperty("idle_time_minute")
    private float idleTimeMinute;
    @JsonProperty("power_fail_time_hour")
    private float powerFailTimeHour;
    @JsonProperty("power_fail_time_minute")
    private float powerFailTimeMinute;
    @JsonProperty("doff_time_hour")
    private float doffTimeHour;
    @JsonProperty("doff_time_minute")
    private float doffTimeMinute;

    // Machine efficiency and performance fields
    @JsonProperty("counts")
    private float counts;
    @JsonProperty("average_tpi")
    private float averageTpi;
    @JsonProperty("average_spindle_speed")
    private float averageSpindleSpeed;
    @JsonProperty("average_delivery")
    private float averageDelivery;

    // Power-related fields
    @JsonProperty("voltage_avg")
    private float voltageAvg;
    @JsonProperty("current_avg")
    private float currentAvg;
    @JsonProperty("power_factor")
    private float powerFactor;
    @JsonProperty("frequency")
    private float frequency;
    @JsonProperty("total_active_power")
    private float totalActivePower;
    @JsonProperty("total_kva")
    private float totalKva;
    @JsonProperty("total_kvar")
    private float totalKvar;
    @JsonProperty("total_kwh")
    private float totalKwh;
    @JsonProperty("total_kvah")
    private float totalKvah;
    @JsonProperty("total_kvarh")
    private float totalKvarh;

    //
    @JsonProperty("overall_machine_utilization")
    private float overallMachineUtilization;
    @JsonProperty("production_efficiency")
    private float productionEfficiency;
    @JsonProperty("production_kg")
    private float productionKg;
    @JsonProperty("ukg")
    private float ukg;
    @JsonProperty("gpss")
    private float gpss;

    public RingFrame40(String tenant, String business, String unit, String shed, String department, String asset) {
        super(tenant, business, unit, shed, department, asset);
    }

    public void generateRandomDate() {
        Random random = new Random();
        // Generate timestamps
        this.ts = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.tsAct = String.valueOf(new Timestamp(System.currentTimeMillis()));

        // Generate shift-related fields
        shiftNumber = random.nextFloat() * 10; // Shift numbers can be between 0 and 10
        shiftDate = random.nextInt(31) + 1; // Day of the month (1-31)
        shiftMonth = random.nextInt(12) + 1; // Month of the year (1-12)
        shiftYear = random.nextInt(2023 - 2020 + 1) + 2020; // Year range from 2020 to 2023
        shiftHour = random.nextInt(24); // Hour (0-23)
        shiftMinute = random.nextInt(60); // Minute (0-59)

        // Generate production fields
        productionInHanks = random.nextFloat() * 100; // Production in hanks
        productionInMeters = productionInHanks * 100; // Assuming each hank is 100 meters
        weightPerSpindle = random.nextFloat() * 10; // Weight per spindle (in kg)
        noOfDoffs = random.nextInt(20); // Number of doffs
        airConsumption = random.nextFloat() * 50; // Air consumption (in cubic meters)

        // Generate runtime fields
        runTimeHour = random.nextInt(24); // Runtime hours
        runTimeMinute = random.nextInt(60); // Runtime minutes
        idleTimeHour = random.nextInt(5); // Idle time (0-4 hours)
        idleTimeMinute = random.nextInt(60); // Idle minutes
        powerFailTimeHour = random.nextInt(3); // Power failure time (0-2 hours)
        powerFailTimeMinute = random.nextInt(60); // Power failure minutes
        doffTimeHour = random.nextInt(2); // Doff time (0-1 hour)
        doffTimeMinute = random.nextInt(60); // Doff minutes

        // Generate efficiency and performance fields
        counts = random.nextFloat() * 1000; // Count
        averageTpi = random.nextFloat() * 20; // Average TPI (Twists Per Inch)
        averageSpindleSpeed = random.nextFloat() * 3000; // Average spindle speed (in RPM)
        averageDelivery = random.nextFloat() * 100; // Average delivery (in kg)

        // Generate power-related fields
        voltageAvg = random.nextFloat() * 400; // Voltage (0-400V)
        currentAvg = random.nextFloat() * 200; // Current (0-200A)
        powerFactor = random.nextFloat(); // Power factor (0-1)
        frequency = random.nextFloat() * 50; // Frequency (0-50Hz)
        totalActivePower = voltageAvg * currentAvg; // Total active power
        totalKva = totalActivePower / powerFactor; // Total KVA
        totalKvar = (float) Math.sqrt(Math.pow(totalKva, 2) - Math.pow(totalActivePower, 2)); // Total KVAR
        totalKwh = totalActivePower * (runTimeHour + runTimeMinute / 60.0f); // Total KWH
        totalKvah = totalKva * (runTimeHour + runTimeMinute / 60.0f); // Total KVAH
        totalKvarh = totalKvar * (runTimeHour + runTimeMinute / 60.0f); // Total KVARH

        // Generate efficiency fields
        overallMachineUtilization = random.nextFloat(); // Overall machine utilization (0-1)
        productionEfficiency = random.nextFloat(); // Production efficiency (0-1)
        productionKg = productionInMeters * weightPerSpindle; // Production in kg
        ukg = random.nextFloat() * 100; // Some random value for UKG
        gpss = random.nextFloat() * 10; // Some random value for GPSS
    }
}