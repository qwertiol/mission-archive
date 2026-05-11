package com.tokyomagic.lab3missionarchive.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tokyomagic.lab3missionarchive.model.enums.MissionOutcome;
import java.time.LocalDate;
import java.util.List;

public class Mission {
    private final String missionId;
    private final LocalDate date;
    private final String location;
    private final MissionOutcome outcome;
    private final long damageCost;
    private final Curse curse;
    private final List<Sorcerer> sorcerers;
    private final List<Technique> techniques;
    private final EconomicAssessment economicAssessment;
    private final EnemyActivity enemyActivity;
    private final EnvironmentConditions environmentConditions;
    private final CivilianImpact civilianImpact;
    private final List<TimelineEvent> timelineEvents;
    private final String comment;

    // Приватный конструктор, используемый билдером
    private Mission(Builder builder) {
        this.missionId = builder.missionId;
        this.date = builder.date;
        this.location = builder.location;
        this.outcome = builder.outcome;
        this.damageCost = builder.damageCost;
        this.curse = builder.curse;
        this.sorcerers = builder.sorcerers;
        this.techniques = builder.techniques;
        this.economicAssessment = builder.economicAssessment;
        this.enemyActivity = builder.enemyActivity;
        this.environmentConditions = builder.environmentConditions;
        this.civilianImpact = builder.civilianImpact;
        this.timelineEvents = builder.timelineEvents;
        this.comment = builder.comment;
    }

    // Новый метод для Jackson (JSON/YAML/XML)
    @JsonCreator
    public static Mission create(
            @JsonProperty("missionId") String missionId,
            @JsonProperty("date") LocalDate date,
            @JsonProperty("location") String location,
            @JsonProperty("outcome") MissionOutcome outcome,
            @JsonProperty("damageCost") long damageCost,
            @JsonProperty("curse") Curse curse,
            @JsonProperty("sorcerers") List<Sorcerer> sorcerers,
            @JsonProperty("techniques") List<Technique> techniques,
            @JsonProperty("economicAssessment") EconomicAssessment economicAssessment,
            @JsonProperty("enemyActivity") EnemyActivity enemyActivity,
            @JsonProperty("environmentConditions") EnvironmentConditions environmentConditions,
            @JsonProperty("civilianImpact") CivilianImpact civilianImpact,
            @JsonProperty("timelineEvents") List<TimelineEvent> timelineEvents,
            @JsonProperty("comment") String comment
    ) {
        // Пробуем новую штучку, называется fluent interface
        Builder builder = new Builder()
                .missionId(missionId)
                .date(date)
                .location(location)
                .outcome(outcome)
                .damageCost(damageCost)
                .curse(curse)
                .sorcerers(sorcerers != null ? sorcerers : List.of())
                .techniques(techniques != null ? techniques : List.of())
                .economicAssessment(economicAssessment)
                .enemyActivity(enemyActivity)
                .environmentConditions(environmentConditions)
                .civilianImpact(civilianImpact)
                .timelineEvents(timelineEvents)
                .comment(comment);
        return builder.build();
    }

    // Геттеры
    public String getMissionId() { return missionId; }
    public LocalDate getDate() { return date; }
    public String getLocation() { return location; }
    public MissionOutcome getOutcome() { return outcome; }
    public long getDamageCost() { return damageCost; }
    public Curse getCurse() { return curse; }
    public List<Sorcerer> getSorcerers() { return sorcerers; }
    public List<Technique> getTechniques() { return techniques; }
    public EconomicAssessment getEconomicAssessment() { return economicAssessment; }
    public EnemyActivity getEnemyActivity() { return enemyActivity; }
    public EnvironmentConditions getEnvironmentConditions() { return environmentConditions; }
    public CivilianImpact getCivilianImpact() { return civilianImpact; }
    public List<TimelineEvent> getTimelineEvents() { return timelineEvents; }
    public String getComment() { return comment; }

    // Вложенный Builder
    public static class Builder {
        private String missionId;
        private LocalDate date;
        private String location;
        private MissionOutcome outcome;
        private long damageCost;
        private Curse curse;
        private List<Sorcerer> sorcerers;
        private List<Technique> techniques;
        private EconomicAssessment economicAssessment;
        private EnemyActivity enemyActivity;
        private EnvironmentConditions environmentConditions;
        private CivilianImpact civilianImpact;
        private List<TimelineEvent> timelineEvents;
        private String comment;

        public Builder missionId(String missionId) { this.missionId = missionId; return this; }
        public Builder date(LocalDate date) { this.date = date; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder outcome(MissionOutcome outcome) { this.outcome = outcome; return this; }
        public Builder damageCost(long damageCost) { this.damageCost = damageCost; return this; }
        public Builder curse(Curse curse) { this.curse = curse; return this; }
        public Builder sorcerers(List<Sorcerer> sorcerers) { this.sorcerers = sorcerers; return this; }
        public Builder techniques(List<Technique> techniques) { this.techniques = techniques; return this; }
        public Builder economicAssessment(EconomicAssessment economicAssessment) { this.economicAssessment = economicAssessment; return this; }
        public Builder enemyActivity(EnemyActivity enemyActivity) { this.enemyActivity = enemyActivity; return this; }
        public Builder environmentConditions(EnvironmentConditions environmentConditions) { this.environmentConditions = environmentConditions; return this; }
        public Builder civilianImpact(CivilianImpact civilianImpact) { this.civilianImpact = civilianImpact; return this; }
        public Builder timelineEvents(List<TimelineEvent> timelineEvents) { this.timelineEvents = timelineEvents; return this; }
        public Builder comment(String comment) { this.comment = comment; return this; }

        public Mission build() {
            if (missionId == null || date == null || location == null || outcome == null || curse == null) {
                throw new IllegalStateException("Missing required mission fields");
            }
            if (sorcerers == null) sorcerers = List.of();
            if (techniques == null) techniques = List.of();
            return new Mission(this);
        }
    }
}