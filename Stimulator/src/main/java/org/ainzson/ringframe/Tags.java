package org.ainzson.ringframe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class Tags {
    // Tagging fields
    @JsonProperty("tenant")
    private String tenant;
    @JsonProperty("business")
    private String business;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("shed")
    private String shed;
    @JsonProperty("department")
    private String department;
    @JsonProperty("asset")
    private String asset;
}
