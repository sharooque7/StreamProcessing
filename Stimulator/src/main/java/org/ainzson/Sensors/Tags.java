package org.ainzson.Sensors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tags {
    @JsonProperty("assetid")
    private String assetId ;
    @JsonProperty("departmentId")
    private String departmentId;
}
