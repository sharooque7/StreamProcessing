package org.ainzson.models.pms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ainzson.models.shift.Shift;
import org.ainzson.utils.BooleanStringDeserializer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Downtime {

    @JsonProperty("tenant_id")
    private String tenantId;

    @JsonProperty("status_code")
    private long statusCode;

    @JsonProperty("start_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime startDatetime;

    @JsonProperty("end_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime endDatetime;

    @JsonProperty("department_id")
    private String departmentId;

    @JsonProperty("shift_start_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime shiftStartDatetime;

    @JsonProperty("cumulative_runtime_count")
    private long cumulativeRuntimeCount;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
    private ZonedDateTime createdAt;

    @JsonProperty("shift_name")
    private String shiftName;

    @JsonProperty("asset_id")
    private String assetId;

    @JsonProperty("planned_downtime")
    private boolean plannedDowntime;

    @JsonProperty("is_deleted")
    @JsonDeserialize(using = BooleanStringDeserializer.class)
    private boolean isDeleted;

    @JsonProperty("planned_shutdown")
    private boolean plannedShutdown;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
    private ZonedDateTime updatedAt;

    @JsonProperty("shift_id")
    private String shiftId;

    @JsonProperty("site_id")
    private String siteId;

    @JsonProperty("shift_end_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime shiftEndDatetime;

    @JsonProperty("sub_tenant_id")
    private String subTenantId;

    private int downtimeCount;
    private int shutDownCount;

    public  Downtime(MachineStatus machineStatus, Shift shiftInfo) {
        ZoneId zoneId = ZoneId.of("UTC");
        ZonedDateTime currentDateTime = ZonedDateTime.ofInstant(machineStatus.getTs(), zoneId);
        this.siteId = machineStatus.getTags().getSite();
        this.assetId = machineStatus.getTags().getAsset();
        this.tenantId = machineStatus.getTags().getTenant();
        this.subTenantId = machineStatus.getTags().getSubtenant();
        this.departmentId = machineStatus.getTags().getDepartment();
        this.statusCode = machineStatus.getStatus();
        this.startDatetime = currentDateTime;
        this.endDatetime = currentDateTime;

        this.shiftStartDatetime = shiftInfo.getStartTime();
        this.shiftEndDatetime = shiftInfo.getEndTime();
        this.createdAt = ZonedDateTime.ofInstant(Instant.now(),zoneId);
        this.shiftName = shiftInfo.getName();
        this.shiftId = shiftInfo.getShiftId();
        this.plannedShutdown = false;
        this.plannedDowntime = false;
        this.shutDownCount = 0;
        this.downtimeCount = 0;

    }
}
