package com.tokyomagic.lab3missionarchive.model;

import com.mycompany.lab2missionanalyzerpatterns.model.enums.PublicExposureRisk;

public class CivilianImpact {
    private Integer evacuated;
    private Integer injured;
    private Integer missing;
    private PublicExposureRisk publicExposureRisk;

    public CivilianImpact() {}

    public Integer getEvacuated() { return evacuated; }
    public void setEvacuated(Integer evacuated) { this.evacuated = evacuated; }

    public Integer getInjured() { return injured; }
    public void setInjured(Integer injured) { this.injured = injured; }

    public Integer getMissing() { return missing; }
    public void setMissing(Integer missing) { this.missing = missing; }

    public PublicExposureRisk getPublicExposureRisk() { return publicExposureRisk; }
    public void setPublicExposureRisk(PublicExposureRisk risk) { this.publicExposureRisk = risk; }
    public void setPublicExposureRiskString(String riskStr) {
        this.publicExposureRisk = PublicExposureRisk.fromString(riskStr);
    }
}