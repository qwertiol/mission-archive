package com.tokyomagic.lab3missionarchive.entity;

import com.tokyomagic.lab3missionarchive.model.enums.MissionOutcome;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "missions")
public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String missionId;

    private LocalDate date;
    private String location;

    @Enumerated(EnumType.STRING)
    private MissionOutcome outcome;

    private long damageCost;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "curse_id")
    private CurseEntity curse;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "economic_assessment_id")
    private EconomicAssessmentEntity economicAssessment;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "enemy_activity_id")
    private EnemyActivityEntity enemyActivity;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "environment_conditions_id")
    private EnvironmentConditionsEntity environmentConditions;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "civilian_impact_id")
    private CivilianImpactEntity civilianImpact;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SorcererEntity> sorcerers = new ArrayList<>();

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechniqueEntity> techniques = new ArrayList<>();

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimelineEventEntity> timelineEvents = new ArrayList<>();

    private String comment;

    public MissionEntity() {}

    // геттеры, сеттеры и методы синхронизации связей
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMissionId() { return missionId; }
    public void setMissionId(String missionId) { this.missionId = missionId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public MissionOutcome getOutcome() { return outcome; }
    public void setOutcome(MissionOutcome outcome) { this.outcome = outcome; }

    public long getDamageCost() { return damageCost; }
    public void setDamageCost(long damageCost) { this.damageCost = damageCost; }

    public CurseEntity getCurse() { return curse; }
    public void setCurse(CurseEntity curse) { this.curse = curse; }

    public EconomicAssessmentEntity getEconomicAssessment() { return economicAssessment; }
    public void setEconomicAssessment(EconomicAssessmentEntity economicAssessment) { this.economicAssessment = economicAssessment; }

    public EnemyActivityEntity getEnemyActivity() { return enemyActivity; }
    public void setEnemyActivity(EnemyActivityEntity enemyActivity) { this.enemyActivity = enemyActivity; }

    public EnvironmentConditionsEntity getEnvironmentConditions() { return environmentConditions; }
    public void setEnvironmentConditions(EnvironmentConditionsEntity environmentConditions) { this.environmentConditions = environmentConditions; }

    public CivilianImpactEntity getCivilianImpact() { return civilianImpact; }
    public void setCivilianImpact(CivilianImpactEntity civilianImpact) { this.civilianImpact = civilianImpact; }

    public List<SorcererEntity> getSorcerers() { return sorcerers; }
    public void setSorcerers(List<SorcererEntity> sorcerers) {
        this.sorcerers.clear();
        if (sorcerers != null) {
            sorcerers.forEach(this::addSorcerer);
        }
    }

    public List<TechniqueEntity> getTechniques() { return techniques; }
    public void setTechniques(List<TechniqueEntity> techniques) {
        this.techniques.clear();
        if (techniques != null) {
            techniques.forEach(this::addTechnique);
        }
    }

    public List<TimelineEventEntity> getTimelineEvents() { return timelineEvents; }
    public void setTimelineEvents(List<TimelineEventEntity> timelineEvents) {
        this.timelineEvents.clear();
        if (timelineEvents != null) {
            timelineEvents.forEach(this::addTimelineEvent);
        }
    }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public void addSorcerer(SorcererEntity sorcerer) {
        sorcerers.add(sorcerer);
        sorcerer.setMission(this);
    }

    public void removeSorcerer(SorcererEntity sorcerer) {
        sorcerers.remove(sorcerer);
        sorcerer.setMission(null);
    }

    public void addTechnique(TechniqueEntity technique) {
        techniques.add(technique);
        technique.setMission(this);
    }

    public void removeTechnique(TechniqueEntity technique) {
        techniques.remove(technique);
        technique.setMission(null);
    }

    public void addTimelineEvent(TimelineEventEntity event) {
        timelineEvents.add(event);
        event.setMission(this);
    }

    public void removeTimelineEvent(TimelineEventEntity event) {
        timelineEvents.remove(event);
        event.setMission(null);
    }
}