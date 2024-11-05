package org.ainzson.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class Tags {

    @JsonProperty("asset")
    private String asset;
    @JsonProperty("tenant")
    private String tenant;
    @JsonProperty("site")
    private String site;
    @JsonProperty("subtenant")
    private String subtenant;
    @JsonProperty("cell")
    private String cell;
    @JsonProperty("department")
    private String department;

    @Override
    public String toString() {
        return "Tags{" +
                "asset='" + asset + '\'' +
                ", tenant='" + tenant + '\'' +
                ", site='" + site + '\'' +
                ", subtenant='" + subtenant + '\'' +
                ", cell='" + cell + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}