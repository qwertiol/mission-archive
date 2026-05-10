package com.tokyomagic.lab3missionarchive.model;

public class EconomicAssessment {
    private Long totalDamageCost;
    private Long infrastructureDamage;
    private Long commercialDamage;
    private Long transportDamage;
    private Integer recoveryEstimateDays;
    private Boolean insuranceCovered;

    public EconomicAssessment() {}

    // геттеры и сеттеры
    public Long getTotalDamageCost() { return totalDamageCost; }
    
    public void setTotalDamageCost(Long totalDamageCost) {
        if (totalDamageCost < 0) {
                throw new IllegalArgumentException("Damage cost can't be less than 0");
        }
        this.totalDamageCost = totalDamageCost; 
    }
    
    public Long getInfrastructureDamage() { return infrastructureDamage; }
    public void setInfrastructureDamage(Long infrastructureDamage) { this.infrastructureDamage = infrastructureDamage; }
    public Long getCommercialDamage() { return commercialDamage; }
    public void setCommercialDamage(Long commercialDamage) { this.commercialDamage = commercialDamage; }
    public Long getTransportDamage() { return transportDamage; }
    public void setTransportDamage(Long transportDamage) { this.transportDamage = transportDamage; }
    public Integer getRecoveryEstimateDays() { return recoveryEstimateDays; }
    public void setRecoveryEstimateDays(Integer recoveryEstimateDays) { this.recoveryEstimateDays = recoveryEstimateDays; }
    public Boolean getInsuranceCovered() { return insuranceCovered; }
    public void setInsuranceCovered(Boolean insuranceCovered) { this.insuranceCovered = insuranceCovered; }
}