package com.tokyomagic.lab3missionarchive.entity;

import com.tokyomagic.lab3missionarchive.model.enums.ThreatLevel;
import jakarta.persistence.*;

@Entity
@Table(name = "curses")
public class CurseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ThreatLevel threatLevel;

    public CurseEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ThreatLevel getThreatLevel() { return threatLevel; }
    public void setThreatLevel(ThreatLevel threatLevel) { this.threatLevel = threatLevel; }
}