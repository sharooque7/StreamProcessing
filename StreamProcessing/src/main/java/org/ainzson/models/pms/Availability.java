package org.ainzson.models.pms;


import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ainzson.config.RedisConfig;
import org.ainzson.models.shift.PlannedDowntime;
import org.ainzson.models.shift.Shift;
import org.ainzson.models.shift.Shutdown;
import org.ainzson.streamprocessor.pms.utils.ShiftUtilities;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Availability {

    @JsonProperty("tenant_id")
    private String tenantId;

    @JsonProperty("created_at")
    private ZonedDateTime createdAt;
    @JsonProperty("asset_id")
    private String assetId;
    @JsonProperty("data_loss")
    private long dataLoss;
    @JsonProperty("availability")
    private int availability;
    @JsonProperty("planned_production_time_seconds")
    private long plannedProductionTimeSeconds;
    @JsonProperty("start_datetime")
    private ZonedDateTime startDatetime;

    @JsonProperty("end_datetime")
    private ZonedDateTime endDatetime;

    @JsonProperty("stopped_time_seconds")
    private long stoppedTimeSeconds;

    @JsonProperty("is_deleted")
    private boolean isDeleted;

    @JsonProperty("updated_at")
    private ZonedDateTime updatedAt;

    @JsonProperty("shift_id")
    private String shiftId;

    @JsonProperty("department_id")
    private String departmentId;

    @JsonProperty("shift_start_datetime")
    private ZonedDateTime shiftStartDatetime;

    @JsonProperty("shift_name")
    private String shiftName;

    @JsonProperty("cuttingtime")
    private long cuttingTime;

    @JsonProperty("minute")
    private ZonedDateTime minute;

    @JsonProperty("planned_downtime")
    private boolean plannedDowntime;

    @JsonProperty("planned_shutdown")
    private boolean plannedShutdown;

    @JsonProperty("run_time_seconds")
    private long runTimeSeconds;

    @JsonProperty("site_id")
    private String siteId;

    @JsonProperty("shift_end_datetime")
    private ZonedDateTime shiftEndDatetime;

    @JsonProperty("sub_tenant_id")
    private String subTenantId;

    @JsonProperty("idle_time_seconds")
    private long idleTimeSeconds;

    @JsonIgnore
    private final int intervalPeriod = 5;
    @JsonIgnore
    private int runTimeCount;
    @JsonIgnore
    private int idleTimeCount;
    @JsonIgnore
    private int stoppedTimeCount;

    public Availability calculate() {return  this;}


    public Availability process(MachineStatus machineStatus) {

        this.subTenantId = machineStatus.getTags().getSubtenant();
        this.tenantId = machineStatus.getTags().getTenant();
        this.assetId = machineStatus.getTags().getAsset();
        this.departmentId = machineStatus.getTags().getDepartment();
        this.siteId = machineStatus.getTags().getSite();

        // Increment existing counts
        if (machineStatus.getStatus() == 1) {
            this.runTimeCount += 1;
            this.runTimeSeconds = (long) this.runTimeCount * this.intervalPeriod;
        } else if (machineStatus.getStatus() == 0) {
            this.stoppedTimeCount += 1;
            this.stoppedTimeSeconds = (long) this.stoppedTimeCount * this.intervalPeriod;
        } else {
            this.idleTimeCount += 1;
            this.idleTimeSeconds = (long) this.idleTimeCount * this.intervalPeriod;
        }

        // Recalculate planned production time
        this.plannedProductionTimeSeconds = this.runTimeSeconds + this.stoppedTimeSeconds + this.idleTimeSeconds;

        // Update availability percentage (handle division by zero)
        this.availability = this.plannedProductionTimeSeconds > 0
                ? (int) ((double) this.runTimeSeconds / this.plannedProductionTimeSeconds * 100)
                : 0;

        return this;
    }

    public static Availability processShift(Long start, Long end, Availability availability, ShiftUtilities shiftUtilities, RedisConfig redisConfig) {
        ZonedDateTime starTime  = Instant.ofEpochMilli(start)
                .atZone(ZoneId.of("UTC"));
        ZonedDateTime endTime  = Instant.ofEpochMilli(end)
                .atZone(ZoneId.of("UTC"));

        MachineStatus machineStatus = new MachineStatus();
        Map<String,Downtime> downtimes = new HashMap<>();
        Tags tags = new Tags();
        tags.setAsset(availability.getAssetId());
        machineStatus.setTags(tags);
        machineStatus.setTs(Instant.ofEpochMilli(start));

        shiftUtilities.checkIsEventFallsWithinShift(machineStatus,redisConfig,downtimes);
        Shift shiftInfo = shiftUtilities.decodeShift(machineStatus,redisConfig,downtimes);
        List<PlannedDowntime> plannedDowntimes = shiftInfo.getPlannedDowntime();
        List<Shutdown> plannedShutdown = shiftInfo.getPlannedShutdown();

        availability.setPlannedDowntime(shiftUtilities.isInDowntimeOrShutdown(Instant.ofEpochMilli(start),plannedDowntimes));
        availability.setPlannedShutdown(shiftUtilities.isInDowntimeOrShutdown(Instant.ofEpochMilli(start),plannedShutdown));

        if (availability.plannedShutdown) {
            availability.setPlannedProductionTimeSeconds(0);
            availability.setAvailability( 0);
        }

        if (availability.plannedDowntime && !availability.plannedShutdown) {
            if (availability.getRunTimeSeconds() > 0) {
                availability.setPlannedProductionTimeSeconds(availability.getRunTimeSeconds());
                availability.setAvailability( (int) (availability.getRunTimeSeconds() / availability.getPlannedProductionTimeSeconds()));
            }
            else {
                availability.setPlannedProductionTimeSeconds(0);
                availability.setAvailability( 0);
            }
        }

        availability.setShiftId(shiftInfo.getShiftId());
        availability.setShiftName(shiftInfo.getName());
        availability.setShiftStartDatetime(shiftInfo.getStartTime());
        availability.setShiftEndDatetime(shiftInfo.getEndTime());
        availability.setStartDatetime(starTime);
        availability.setEndDatetime(endTime);
        availability.setMinute( starTime);

        availability.setUpdatedAt(ZonedDateTime.ofInstant(Instant.now(),ZoneId.of("UTC")));
        availability.setCreatedAt(ZonedDateTime.ofInstant(Instant.now(),ZoneId.of("UTC")));

        return  availability;
    }

}