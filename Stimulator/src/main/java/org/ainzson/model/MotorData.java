package org.ainzson.model;

import java.util.HashMap;

public class MotorData {

    public long ts_act;
    public long ts;
    public  Tags tags;
    public Object sensorData;

    public MotorData(long timestamp, String topic, int current, Tags tags) {
        this.ts = timestamp;
        this.ts_act = timestamp;
        this.tags = tags;
        this.sensorData = new HashMap<String, Integer>() {{
            put(topic,current);
        }};
    }
}
