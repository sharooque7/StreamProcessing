package org.ainzson.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.ainzson.utils.EpochToISO8601Deserializer;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RingFrame40StreamDTO {

    public static class Tags {
        @JsonProperty("location")
        private String location;
        @JsonProperty("groupid")
        private String groupId;
    }

    @JsonProperty("assetid")
    private String assetId;

    @JsonProperty("tags")
    private Tags tags;

    @JsonProperty("ts")
    @JsonDeserialize(using = EpochToISO8601Deserializer.class)
    private String ts;

    @JsonProperty("departmentid")
    private String departmentId;

    @JsonProperty("siteid")
    private String siteId;

    @JsonProperty("machineid")
    private String machineId;

    @JsonProperty("shiftnumber")
    private Integer shiftNumber;

    @JsonProperty("shiftdate")
    @JsonDeserialize(using = EpochToISO8601Deserializer.class)
    private String shiftDate;

    @JsonProperty("shiftmonth")
    private Integer shiftMonth;

    @JsonProperty("shiftyear")
    private Integer shiftYear;

    @JsonProperty("shifthour")
    private Integer shiftHour;

    @JsonProperty("shiftminute")
    private Integer shiftMinute;

    @JsonProperty("productioninhanks")
    private Double productionInHanks;

    @JsonProperty("productioninmeters")
    private Double productionInMeters;

    @JsonProperty("weightperspindle")
    private Double weightPerSpindle;

    @JsonProperty("noofdoffs")
    private Double noOfDoffs;

    @JsonProperty("airconsumption")
    private Double airConsumption;

    @JsonProperty("runtimehour")
    private Double runTimeHour;

    @JsonProperty("runtimeminute")
    private Double runTimeMinute;

    @JsonProperty("idletimehour")
    private Double idleTimeHour;

    @JsonProperty("idletimeminute")
    private Double idleTimeMinute;

    @JsonProperty("powerfailtimehour")
    private Double powerFailTimeHour;

    @JsonProperty("powerfailtimeminute")
    private Double powerFailTimeMinute;

    @JsonProperty("dofftimehour")
    private Double doffTimeHour;

    @JsonProperty("dofftimeminute")
    private Double doffTimeMinute;

    @JsonProperty("counts")
    private Double counts;

    @JsonProperty("averagetpi")
    private Double averageTpi;

    @JsonProperty("averagespindlespeed")
    private Double averageSpindleSpeed;

    @JsonProperty("averagedelivery")
    private Double averageDelivery;

    @JsonProperty("voltageavg")
    private Double voltageAvg;

    @JsonProperty("currentavg")
    private Double currentAvg;

    @JsonProperty("powerfactor")
    private Double powerFactor;

    @JsonProperty("frequency")
    private Double frequency;

    @JsonProperty("totalactivepower")
    private Double totalActivePower;

    @JsonProperty("totalkva")
    private Double totalKva;

    @JsonProperty("totalkvar")
    private Double totalKvar;

    @JsonProperty("totalkwh")
    private Double totalKwh;

    @JsonProperty("totalkvah")
    private Double totalKvah;

    @JsonProperty("totalkvarh")
    private Double totalKvarh;

    @JsonProperty("overallmachineutilization")
    private Double overallMachineUtilization;

    @JsonProperty("productionefficiency")
    private Double productionEfficiency;

    @JsonProperty("productionkg")
    private Double productionKg;

    @JsonProperty("ukg")
    private Double ukg;

    @JsonProperty("gpss")
    private Double gpss;

    @JsonProperty("location")
    private String location;

    @JsonProperty("groupid")
    private Integer groupId;


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("assetid", assetId);
        map.put("ts", ts);
        map.put("departmentid", departmentId);
        map.put("siteid", siteId);
        map.put("machineid", machineId);
        map.put("shiftnumber", shiftNumber);
        map.put("shiftdate", shiftDate);
        map.put("shiftmonth", shiftMonth);
        map.put("shiftyear", shiftYear);
        map.put("shifthour", shiftHour);
        map.put("shiftminute", shiftMinute);
        map.put("productioninhanks", productionInHanks);
        map.put("productioninmeters", productionInMeters);
        map.put("weightperspindle", weightPerSpindle);
        map.put("noofdoffs", noOfDoffs);
        map.put("airconsumption", airConsumption);
        map.put("runtimehour", runTimeHour);
        map.put("runtimeminute", runTimeMinute);
        map.put("idletimehour", idleTimeHour);
        map.put("idletimeminute", idleTimeMinute);
        map.put("powerfailtimehour", powerFailTimeHour);
        map.put("powerfailtimeminute", powerFailTimeMinute);
        map.put("dofftimehour", doffTimeHour);
        map.put("dofftimeminute", doffTimeMinute);
        map.put("counts", counts);
        map.put("averagetpi", averageTpi);
        map.put("averagespindlespeed", averageSpindleSpeed);
        map.put("averagedelivery", averageDelivery);
        map.put("voltageavg", voltageAvg);
        map.put("currentavg", currentAvg);
        map.put("powerfactor", powerFactor);
        map.put("frequency", frequency);
        map.put("totalactivepower", totalActivePower);
        map.put("totalkva", totalKva);
        map.put("totalkvar", totalKvar);
        map.put("totalkwh", totalKwh);
        map.put("totalkvah", totalKvah);
        map.put("totalkvarh", totalKvarh);
        map.put("overallmachineutilization", overallMachineUtilization);
        map.put("productionefficiency", productionEfficiency);
        map.put("productionkg", productionKg);
        map.put("ukg", ukg);
        map.put("gpss", gpss);
        map.put("location", location);
        map.put("groupid", groupId);
        return map;
    }

}
