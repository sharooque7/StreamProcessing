package org.ainzson;

import java.io.Serializable;

public class Space implements Serializable {

    private static final long serialVersionUID = 1L; // Optional, but recommended for versioning

    private int spaceId;
    private String spaceStation;
    private Long timer;

    // Constructors
    public Space() {
    }

    public Space(int spaceId, String spaceStation, Long timer) {
        this.spaceId = spaceId;
        this.spaceStation = spaceStation;
        this.timer = timer;
    }

    // Getters and Setters
    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public String getSpaceStation() {
        return spaceStation;
    }

    public void setSpaceStation(String spaceStation) {
        this.spaceStation = spaceStation;
    }

    public Long getTimer() {
        return timer;
    }

    public void setTimer(Long timer) {
        this.timer = timer;
    }

    // Override toString for easy logging and debugging
    @Override
    public String toString() {
        return "Space{" +
                "spaceId=" + spaceId +
                ", spaceStation='" + spaceStation + '\'' +
                ", timer=" + timer +
                '}';
    }

}
