package com.tokyomagic.lab3missionarchive.entity;

import com.tokyomagic.lab3missionarchive.model.enums.TechniqueType;
import jakarta.persistence.*;

@Entity
@Table(name = "techniques")
public class TechniqueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TechniqueType type;

    private String owner;
    private long damage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private MissionEntity mission;

    public TechniqueEntity() {}

    // геттеры/сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public TechniqueType getType() { return type; }
    public void setType(TechniqueType type) { this.type = type; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public long getDamage() { return damage; }
    public void setDamage(long damage) { this.damage = damage; }
    public MissionEntity getMission() { return mission; }
    public void setMission(MissionEntity mission) { this.mission = mission; }
}