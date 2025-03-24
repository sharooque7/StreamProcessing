package org.ainzson.models.shift;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class PlannedDowntime implements PlannedEvent {
    private String id;
    private String name;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    @Override
    public String toString() {
        return "Shutdown{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
