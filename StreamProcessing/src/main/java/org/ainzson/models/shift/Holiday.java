package org.ainzson.models.shift;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Holiday {
    private String id;
    private String name;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
}