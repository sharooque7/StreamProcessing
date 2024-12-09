package org.ainzson.models.shift;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Shift {
    private String name;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private List<PlannedDowntime> plannedDowntime;
    private List<Shutdown> plannedShutdown;
    private List<Holiday> holidays;
    private String shiftId;

    @Override
    public String toString() {
        return "Shift{" +
                "name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", plannedDowntime=" + plannedDowntime +
                ", plannedShutdown=" + plannedShutdown +
                ", holidays=" + holidays +
                '}';
    }
}
