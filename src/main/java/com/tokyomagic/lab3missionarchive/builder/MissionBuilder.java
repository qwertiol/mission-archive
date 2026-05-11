package com.tokyomagic.lab3missionarchive.builder;

import com.tokyomagic.lab3missionarchive.model.*;
import com.tokyomagic.lab3missionarchive.model.enums.MissionOutcome;
import java.time.LocalDate;
import java.util.List;

public class MissionBuilder {
    private final Mission.Builder builder = new Mission.Builder();

    public MissionBuilder setMissionId(String id) { builder.missionId(id); return this; }
    public MissionBuilder setDate(LocalDate date) { builder.date(date); return this; }
    public MissionBuilder setLocation(String location) { builder.location(location); return this; }

    public MissionBuilder setOutcome(MissionOutcome outcome) { builder.outcome(outcome); return this; }

    public MissionBuilder setOutcomeString(String outcomeStr) {
        builder.outcome(MissionOutcome.fromString(outcomeStr));
        return this;
    }

    public MissionBuilder setDamageCost(long cost) { builder.damageCost(cost); return this; }
    public MissionBuilder setCurse(Curse curse) { builder.curse(curse); return this; }
    public MissionBuilder setSorcerers(List<Sorcerer> sorcerers) { builder.sorcerers(sorcerers); return this; }
    public MissionBuilder setTechniques(List<Technique> techniques) { builder.techniques(techniques); return this; }
    public MissionBuilder setEconomicAssessment(EconomicAssessment ea) { builder.economicAssessment(ea); return this; }
    public MissionBuilder setEnemyActivity(EnemyActivity ea) { builder.enemyActivity(ea); return this; }
    public MissionBuilder setEnvironmentConditions(EnvironmentConditions ec) { builder.environmentConditions(ec); return this; }
    public MissionBuilder setCivilianImpact(CivilianImpact ci) { builder.civilianImpact(ci); return this; }
    public MissionBuilder setTimelineEvents(List<TimelineEvent> events) { builder.timelineEvents(events); return this; }
    public MissionBuilder setComment(String comment) { builder.comment(comment); return this; }

    public Mission build() {
        return builder.build();
    }
}