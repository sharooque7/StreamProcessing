package org.ainzson.models.pms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ainzson.utils.EpochToISO8601Deserializer;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MachineStatus {
    private int status;
    @JsonDeserialize(using = EpochToISO8601Deserializer.class)
    private String ts;
    @JsonDeserialize(using = EpochToISO8601Deserializer.class)
    private String ts_act;
    private String name;
    private Tags tags;

    @Override
    public String toString() {
        return "MachineStatus{" +
                "status=" + status +
                ", ts=" + ts +
                ", ts_act=" + ts_act +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }


}

