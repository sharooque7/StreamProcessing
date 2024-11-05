package org.ainzson.models.cms;

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
    @JsonProperty("department")
    private String departmentId;
}
