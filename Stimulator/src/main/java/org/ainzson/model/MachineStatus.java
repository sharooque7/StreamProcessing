package org.ainzson.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Timestamp;
import java.util.Random;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MachineStatus {

    @JsonProperty("status")
    private int status;

    @JsonProperty("is_processed")
    private boolean isProcessed;

    @JsonProperty("ts")
    private String ts;

    @JsonProperty("ts_act")
    private String ts_act;

    @JsonProperty("tags")
    private Tags tags;

    @JsonProperty("feedrate")
    private float  feedRate;

    @JsonProperty("spindleload")
    private float  spindleLoad;

    @JsonProperty("interlockpmcstatus")
    private int  interLockPMCStatus;

    @JsonProperty("cuttingtime")
    private long  cuttingTime;

    @JsonProperty("spindlespeed")
    private long  spindleSpeed;

    @JsonProperty("partcount")
    private long  partCount;

    @JsonProperty("cycletime")
    private int  cycleTime;

    @JsonProperty("feedrateoverride")
    private int  feedRateOverride;

    public MachineStatus(String asset, String tenant, String site, String subtenant, String department, String cell) {
        this.setTags(new Tags(asset, tenant, site, subtenant, cell,department));
    }

    public void generateRandomData() {
        Random random = new Random();
        this.ts = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.ts_act = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.status = 1;
        this.isProcessed = false;

        this.feedRate = random.nextFloat() * 100; // e.g., a value between 0 and 100
        this.spindleLoad = random.nextFloat() * 100; // e.g., a value between 0 and 100
        this.interLockPMCStatus = random.nextInt(2); // e.g., 0 or 1
        this.cuttingTime = random.nextInt(10000); // e.g., a value between 0 and 10000
        this.spindleLoad = random.nextInt(5000); // e.g., a value between 0 and 5000
        this.partCount = random.nextInt(1000); // e.g., a value between 0 and 1000
        this.cycleTime = random.nextInt(500); // e.g., a value between 0 and 500 seconds
        this.feedRateOverride = random.nextInt(100); // e.g., a percentage between 0 and 100
    }

    @Override
    public String toString() {
        return "MachineStatus{" +
                "status=" + status +
                ", is_processed=" + isProcessed +
                ", ts='" + ts + '\'' +
                ", ts_act='" + ts_act + '\'' +
                ", tags=" + tags +
                ", feedRate=" + feedRate +
                ", spindleLoad=" + spindleLoad +
                ", interLockPMCStatus=" + interLockPMCStatus +
                ", cuttingTime=" + cuttingTime +
                ", spindleSpeed=" + spindleSpeed +
                ", partCount=" + partCount +
                ", cycleTime=" + cycleTime +
                ", feedRateOverride=" + feedRateOverride +
                '}';
    }
}







