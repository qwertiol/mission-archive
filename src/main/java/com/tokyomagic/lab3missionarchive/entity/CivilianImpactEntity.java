package com.tokyomagic.lab3missionarchive.entity;

import com.tokyomagic.lab3missionarchive.model.enums.PublicExposureRisk;
import jakarta.persistence.*;

@Entity
@Table(name = "civilian_impacts")
public class CivilianImpactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer evacuated;
    private Integer injured;
    private Integer missing;

    @Enumerated(EnumType.STRING)
    private PublicExposureRisk publicExposureRisk;

    public CivilianImpactEntity() {}

    // геттеры/сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getEvacuated() { return evacuated; }
    public void setEvacuated(Integer evacuated) { this.evacuated = evacuated; }
    public Integer getInjured() { return injured; }
    public void setInjured(Integer injured) { this.injured = injured; }
    public Integer getMissing() { return missing; }
    public void setMissing(Integer missing) { this.missing = missing; }
    public PublicExposureRisk getPublicExposureRisk() { return publicExposureRisk; }
    public void setPublicExposureRisk(PublicExposureRisk publicExposureRisk) { this.publicExposureRisk = publicExposureRisk; }
}