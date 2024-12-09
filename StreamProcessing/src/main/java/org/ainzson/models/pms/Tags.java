package org.ainzson.models.pms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private String asset;
    private String tenant;
    private String site;
    private String subtenant;
    private String department;

    @Override
    public String toString() {
        return "Tags{" +
                "asset='" + asset + '\'' +
                ", tenant='" + tenant + '\'' +
                ", site='" + site + '\'' +
                ", subtenant='" + subtenant + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}