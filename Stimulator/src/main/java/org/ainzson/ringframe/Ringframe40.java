package org.ainzson.ringframe;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Random;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ringframe40 {

    private String assetId;
    private Timestamp ts;
    private String departmentId;
    private String siteId;
    private String machineId;
    private Integer shiftNumber;
    private Timestamp shiftDate;
    private Integer shiftMonth;
    private Integer shiftYear;
    private Integer shiftHour;
    private Integer shiftMinute;
    private Double productionInHanks;
    private Double productionInMeters;
    private Double weightPerSpindle;
    private Double noOfDoffs;
    private Double airConsumption;
    private Double runTimeHour;
    private Double runTimeMinute;
    private Double idleTimeHour;
    private Double idleTimeMinute;
    private Double powerFailTimeHour;
    private Double powerFailTimeMinute;
    private Double doffTimeHour;
    private Double doffTimeMinute;
    private Double counts;
    private Double averageTpi;
    private Double averageSpindleSpeed;
    private Double averageDelivery;
    private Double voltageAvg;
    private Double currentAvg;
    private Double powerFactor;
    private Double frequency;
    private Double totalActivePower;
    private Double totalKva;
    private Double totalKvar;
    private Double totalKwh;
    private Double totalKvah;
    private Double totalKvarh;
    private Double overallMachineUtilization;
    private Double productionEfficiency;
    private Double productionKg;
    private Double ukg;
    private Double gpss;
    private String location;
    private Integer groupId;

    public Ringframe40(String assetId,String departmentId,String siteId) {
        this.assetId = assetId;
        this.departmentId = departmentId;
        this.siteId = siteId;
        this.machineId = assetId;
        generateRandomDate();
    }
    public void generateRandomDate() {
        Random random = new Random();
        this.ts = new Timestamp(System.currentTimeMillis());
        this.shiftDate = new Timestamp(System.currentTimeMillis());

        this.shiftNumber = 1;
        this.shiftMonth = String.valueOf(LocalDate.now().getMonth()).length();
        this.shiftYear = LocalDate.now().getYear();
        this.shiftHour = LocalDateTime.now().getHour();
        this.shiftMinute  = LocalDateTime.now().getMinute();

        this.productionInHanks = random.nextDouble() * 100;
        this.productionInMeters = random.nextDouble() * 100;
        this.weightPerSpindle = random.nextDouble() * 100;
        this.noOfDoffs = random.nextDouble() * 100;
        this. airConsumption = random.nextDouble() * 100;
        this.runTimeHour=random.nextDouble() * 10;
        this.runTimeMinute=random.nextDouble() * 10;
        this.idleTimeHour=random.nextDouble() * 10;
        this.idleTimeMinute=random.nextDouble() * 10;
        this.powerFailTimeHour=random.nextDouble() * 10;
        this.powerFailTimeMinute=random.nextDouble() * 10;
        this.doffTimeHour=random.nextDouble() * 10;
        this.doffTimeMinute=random.nextDouble() * 10;
        this.counts=random.nextDouble() * 10;
        this.averageTpi=random.nextDouble() * 10;
        this.averageSpindleSpeed=random.nextDouble() * 10;
        this.averageDelivery=random.nextDouble() * 10;
        this.voltageAvg=random.nextDouble() * 10;
        this.currentAvg=random.nextDouble() * 10;
        this.powerFactor=random.nextDouble() * 10;
        this.frequency=random.nextDouble() * 10;
        this.totalActivePower=random.nextDouble() * 10;
        this.totalKva=random.nextDouble() * 10;
        this.totalKvar=random.nextDouble() * 10;
        this.totalKwh=random.nextDouble() * 10;
        this.totalKvah=random.nextDouble() * 10;
        this.totalKvarh=random.nextDouble() * 10;
        this.overallMachineUtilization=random.nextDouble() * 10;
        this.productionEfficiency=random.nextDouble() * 10;
        this.productionKg=random.nextDouble() * 10;
        this.ukg=random.nextDouble() * 10;
        this.gpss=random.nextDouble() * 10;
        this.location= "Chennai";
        this.groupId=random.nextInt() * 10;
    }
}
