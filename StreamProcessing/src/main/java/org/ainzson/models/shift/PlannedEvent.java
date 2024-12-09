package org.ainzson.models.shift;

import java.time.ZonedDateTime;

public interface PlannedEvent {
    ZonedDateTime getStartTime();
    ZonedDateTime getEndTime();
}
