package org.ainzson.schema;


import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RingFrame40StreamDTO {


    @SerializedName("assetid")
    private String assetId;

    @SerializedName("ts")
    private Timestamp ts;

    @SerializedName("departmentid")
    private String departmentId;

    @SerializedName("siteid")
    private String siteId;

    @SerializedName("machineid")
    private String machineId;

    @SerializedName("shiftnumber")
    private Integer shiftNumber;

    @SerializedName("shiftdate")
    private Timestamp shiftDate;

    @SerializedName("shiftmonth")
    private Integer shiftMonth;

    @SerializedName("shiftyear")
    private Integer shiftYear;

    @SerializedName("shifthour")
    private Integer shiftHour;

    @SerializedName("shiftminute")
    private Integer shiftMinute;

    @SerializedName("productioninhanks")
    private Double productionInHanks;

    @SerializedName("productioninmeters")
    private Double productionInMeters;

    @SerializedName("weightperspindle")
    private Double weightPerSpindle;

    @SerializedName("noofdoffs")
    private Double noOfDoffs;

    @SerializedName("airconsumption")
    private Double airConsumption;

    @SerializedName("runtimehour")
    private Double runTimeHour;

    @SerializedName("runtimeminute")
    private Double runTimeMinute;

    @SerializedName("idletimehour")
    private Double idleTimeHour;

    @SerializedName("idletimeminute")
    private Double idleTimeMinute;

    @SerializedName("powerfailtimehour")
    private Double powerFailTimeHour;

    @SerializedName("powerfailtimeminute")
    private Double powerFailTimeMinute;

    @SerializedName("dofftimehour")
    private Double doffTimeHour;

    @SerializedName("dofftimeminute")
    private Double doffTimeMinute;

    @SerializedName("counts")
    private Double counts;

    @SerializedName("averagetpi")
    private Double averageTpi;

    @SerializedName("averagespindlespeed")
    private Double averageSpindleSpeed;

    @SerializedName("averagedelivery")
    private Double averageDelivery;

    @SerializedName("voltageavg")
    private Double voltageAvg;

    @SerializedName("currentavg")
    private Double currentAvg;

    @SerializedName("powerfactor")
    private Double powerFactor;

    @SerializedName("frequency")
    private Double frequency;

    @SerializedName("totalactivepower")
    private Double totalActivePower;

    @SerializedName("totalkva")
    private Double totalKva;

    @SerializedName("totalkvar")
    private Double totalKvar;

    @SerializedName("totalkwh")
    private Double totalKwh;

    @SerializedName("totalkvah")
    private Double totalKvah;

    @SerializedName("totalkvarh")
    private Double totalKvarh;

    @SerializedName("overallmachineutilization")
    private Double overallMachineUtilization;

    @SerializedName("productionefficiency")
    private Double productionEfficiency;

    @SerializedName("productionkg")
    private Double productionKg;

    @SerializedName("ukg")
    private Double ukg;

    @SerializedName("gpss")
    private Double gpss;

    @SerializedName("location")
    private String location;

    @SerializedName("groupid")
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
