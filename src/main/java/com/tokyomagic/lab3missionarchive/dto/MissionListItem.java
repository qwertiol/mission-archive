package com.tokyomagic.lab3missionarchive.dto;

public class MissionListItem {
    private Long id;
    private String missionId;
    private String date;
    private String location;
    private String outcome;

    public MissionListItem() {}

    public MissionListItem(Long id, String missionId, String date, String location, String outcome) {
        this.id = id;
        this.missionId = missionId;
        this.date = date;
        this.location = location;
        this.outcome = outcome;
    }

    // геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMissionId() { return missionId; }
    public void setMissionId(String missionId) { this.missionId = missionId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
}