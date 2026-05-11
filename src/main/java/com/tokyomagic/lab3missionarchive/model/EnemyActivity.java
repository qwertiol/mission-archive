package com.tokyomagic.lab3missionarchive.model;

import com.tokyomagic.lab3missionarchive.model.enums.EscalationRisk;
import com.tokyomagic.lab3missionarchive.model.enums.Mobility;
import java.util.List;

public class EnemyActivity {
    private String behaviorType;
    private String targetPriority;
    private Mobility mobility;
    private EscalationRisk escalationRisk;
    private List<String> attackPatterns;
    private List<String> countermeasuresUsed;

    public EnemyActivity() {}

    public String getBehaviorType() { return behaviorType; }
    public void setBehaviorType(String behaviorType) { this.behaviorType = behaviorType; }

    public String getTargetPriority() { return targetPriority; }
    public void setTargetPriority(String targetPriority) { this.targetPriority = targetPriority; }

    public Mobility getMobility() { return mobility; }
    public void setMobility(Mobility mobility) { this.mobility = mobility; }
    public void setMobilityString(String mobilityStr) {
        this.mobility = Mobility.fromString(mobilityStr);
    }

    public EscalationRisk getEscalationRisk() { return escalationRisk; }
    public void setEscalationRisk(EscalationRisk escalationRisk) { this.escalationRisk = escalationRisk; }
    public void setEscalationRiskString(String riskStr) {
        this.escalationRisk = EscalationRisk.fromString(riskStr);
    }

    public List<String> getAttackPatterns() { return attackPatterns; }
    public void setAttackPatterns(List<String> attackPatterns) { this.attackPatterns = attackPatterns; }

    public List<String> getCountermeasuresUsed() { return countermeasuresUsed; }
    public void setCountermeasuresUsed(List<String> countermeasuresUsed) { this.countermeasuresUsed = countermeasuresUsed; }
}