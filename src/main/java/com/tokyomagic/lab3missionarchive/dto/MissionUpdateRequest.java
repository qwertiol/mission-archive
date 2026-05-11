package com.tokyomagic.lab3missionarchive.dto;

import com.tokyomagic.lab3missionarchive.model.enums.MissionOutcome;

// DTO для частичного обновления миссии
public class MissionUpdateRequest {
    private String location;
    private MissionOutcome outcome;
    private Long damageCost;
    private String comment;

    public MissionUpdateRequest() {}

    // геттеры/сеттеры
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public MissionOutcome getOutcome() { return outcome; }
    public void setOutcome(MissionOutcome outcome) { this.outcome = outcome; }
    public Long getDamageCost() { return damageCost; }
    public void setDamageCost(Long damageCost) { this.damageCost = damageCost; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}