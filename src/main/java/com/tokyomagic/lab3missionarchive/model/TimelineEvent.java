package com.tokyomagic.lab3missionarchive.model;

import java.time.LocalDateTime;

public class TimelineEvent {
    private LocalDateTime timestamp;
    private String type;
    private String description;

    public TimelineEvent() {}

    public TimelineEvent(LocalDateTime timestamp, String type, String description) {
        this.timestamp = timestamp;
        this.type = type;
        this.description = description;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}