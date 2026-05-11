package com.tokyomagic.lab3missionarchive.dto;

import java.util.List;

public class MissionDetailDto {
    private Long id;
    private String missionId;
    private String date;
    private String location;
    private String outcome;
    private long damageCost;
    private CurseDto curse;
    private EconomicAssessmentDto economicAssessment;
    private EnemyActivityDto enemyActivity;
    private EnvironmentConditionsDto environmentConditions;
    private CivilianImpactDto civilianImpact;
    private List<SorcererDto> sorcerers;
    private List<TechniqueDto> techniques;
    private List<TimelineEventDto> timelineEvents;
    private String comment;

    public MissionDetailDto() {}

    // геттеры и сеттеры для всех полей
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
    public long getDamageCost() { return damageCost; }
    public void setDamageCost(long damageCost) { this.damageCost = damageCost; }
    public CurseDto getCurse() { return curse; }
    public void setCurse(CurseDto curse) { this.curse = curse; }
    public EconomicAssessmentDto getEconomicAssessment() { return economicAssessment; }
    public void setEconomicAssessment(EconomicAssessmentDto economicAssessment) { this.economicAssessment = economicAssessment; }
    public EnemyActivityDto getEnemyActivity() { return enemyActivity; }
    public void setEnemyActivity(EnemyActivityDto enemyActivity) { this.enemyActivity = enemyActivity; }
    public EnvironmentConditionsDto getEnvironmentConditions() { return environmentConditions; }
    public void setEnvironmentConditions(EnvironmentConditionsDto environmentConditions) { this.environmentConditions = environmentConditions; }
    public CivilianImpactDto getCivilianImpact() { return civilianImpact; }
    public void setCivilianImpact(CivilianImpactDto civilianImpact) { this.civilianImpact = civilianImpact; }
    public List<SorcererDto> getSorcerers() { return sorcerers; }
    public void setSorcerers(List<SorcererDto> sorcerers) { this.sorcerers = sorcerers; }
    public List<TechniqueDto> getTechniques() { return techniques; }
    public void setTechniques(List<TechniqueDto> techniques) { this.techniques = techniques; }
    public List<TimelineEventDto> getTimelineEvents() { return timelineEvents; }
    public void setTimelineEvents(List<TimelineEventDto> timelineEvents) { this.timelineEvents = timelineEvents; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public static class CurseDto {
        private String name;
        private String threatLevel;
        public CurseDto() {}
        public CurseDto(String name, String threatLevel) { this.name = name; this.threatLevel = threatLevel; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getThreatLevel() { return threatLevel; }
        public void setThreatLevel(String threatLevel) { this.threatLevel = threatLevel; }
    }

    public static class SorcererDto {
        private String name;
        private String rank;
        public SorcererDto() {}
        public SorcererDto(String name, String rank) { this.name = name; this.rank = rank; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRank() { return rank; }
        public void setRank(String rank) { this.rank = rank; }
    }

    public static class TechniqueDto {
        private String name;
        private String type;
        private String owner;
        private long damage;
        public TechniqueDto() {}
        public TechniqueDto(String name, String type, String owner, long damage) {
            this.name = name; this.type = type; this.owner = owner; this.damage = damage;
        }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getOwner() { return owner; }
        public void setOwner(String owner) { this.owner = owner; }
        public long getDamage() { return damage; }
        public void setDamage(long damage) { this.damage = damage; }
    }

    public static class EconomicAssessmentDto {
        private Long totalDamageCost;
        private Long infrastructureDamage;
        private Long commercialDamage;
        private Long transportDamage;
        private Integer recoveryEstimateDays;
        private Boolean insuranceCovered;
        public EconomicAssessmentDto() {}
        // геттеры и сеттеры
        public Long getTotalDamageCost() { return totalDamageCost; }
        public void setTotalDamageCost(Long totalDamageCost) { this.totalDamageCost = totalDamageCost; }
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

    public static class EnemyActivityDto {
        private String behaviorType;
        private String targetPriority;
        private String mobility;
        private String escalationRisk;
        private List<String> attackPatterns;
        private List<String> countermeasuresUsed;
        public EnemyActivityDto() {}
        // геттеры и сеттеры
        public String getBehaviorType() { return behaviorType; }
        public void setBehaviorType(String behaviorType) { this.behaviorType = behaviorType; }
        public String getTargetPriority() { return targetPriority; }
        public void setTargetPriority(String targetPriority) { this.targetPriority = targetPriority; }
        public String getMobility() { return mobility; }
        public void setMobility(String mobility) { this.mobility = mobility; }
        public String getEscalationRisk() { return escalationRisk; }
        public void setEscalationRisk(String escalationRisk) { this.escalationRisk = escalationRisk; }
        public List<String> getAttackPatterns() { return attackPatterns; }
        public void setAttackPatterns(List<String> attackPatterns) { this.attackPatterns = attackPatterns; }
        public List<String> getCountermeasuresUsed() { return countermeasuresUsed; }
        public void setCountermeasuresUsed(List<String> countermeasuresUsed) { this.countermeasuresUsed = countermeasuresUsed; }
    }

    public static class EnvironmentConditionsDto {
        private String weather;
        private String timeOfDay;
        private String visibility;
        private Integer cursedEnergyDensity;
        public EnvironmentConditionsDto() {}
        // геттеры и сеттеры
        public String getWeather() { return weather; }
        public void setWeather(String weather) { this.weather = weather; }
        public String getTimeOfDay() { return timeOfDay; }
        public void setTimeOfDay(String timeOfDay) { this.timeOfDay = timeOfDay; }
        public String getVisibility() { return visibility; }
        public void setVisibility(String visibility) { this.visibility = visibility; }
        public Integer getCursedEnergyDensity() { return cursedEnergyDensity; }
        public void setCursedEnergyDensity(Integer cursedEnergyDensity) { this.cursedEnergyDensity = cursedEnergyDensity; }
    }

    public static class CivilianImpactDto {
        private Integer evacuated;
        private Integer injured;
        private Integer missing;
        private String publicExposureRisk;
        public CivilianImpactDto() {}
        // геттеры и сеттеры
        public Integer getEvacuated() { return evacuated; }
        public void setEvacuated(Integer evacuated) { this.evacuated = evacuated; }
        public Integer getInjured() { return injured; }
        public void setInjured(Integer injured) { this.injured = injured; }
        public Integer getMissing() { return missing; }
        public void setMissing(Integer missing) { this.missing = missing; }
        public String getPublicExposureRisk() { return publicExposureRisk; }
        public void setPublicExposureRisk(String publicExposureRisk) { this.publicExposureRisk = publicExposureRisk; }
    }

    public static class TimelineEventDto {
        private String timestamp;
        private String type;
        private String description;
        public TimelineEventDto() {}
        public TimelineEventDto(String timestamp, String type, String description) {
            this.timestamp = timestamp; this.type = type; this.description = description;
        }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}