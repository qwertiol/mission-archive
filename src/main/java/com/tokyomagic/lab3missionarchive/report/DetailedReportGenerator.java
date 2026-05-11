package com.tokyomagic.lab3missionarchive.report;

import com.tokyomagic.lab3missionarchive.entity.*;

public class DetailedReportGenerator implements ReportGenerator {
    @Override
    public String generate(MissionEntity mission) {
        StringBuilder sb = new StringBuilder();
        sb.append("~~~ Mission Detailed Report ~~~\n");
        sb.append("Mission ID: ").append(mission.getMissionId()).append("\n");
        sb.append("Date: ").append(mission.getDate()).append("\n");
        sb.append("Location: ").append(mission.getLocation()).append("\n");
        sb.append("Outcome: ").append(mission.getOutcome()).append("\n");
        sb.append("Damage cost: ").append(mission.getDamageCost()).append("\n");

        if (mission.getCurse() != null) {
            sb.append("Curse: ").append(mission.getCurse().getName())
              .append(" (level: ").append(mission.getCurse().getThreatLevel()).append(")\n");
        }

        sb.append("Participants:\n");
        for (SorcererEntity s : mission.getSorcerers()) {
            sb.append("  - ").append(s.getName())
              .append(" (rank: ").append(s.getRank()).append(")\n");
        }

        sb.append("Techniques used:\n");
        for (TechniqueEntity t : mission.getTechniques()) {
            sb.append("  - ").append(t.getName())
              .append(" (type: ").append(t.getType())
              .append(", owner: ").append(t.getOwner())
              .append(", damage: ").append(t.getDamage()).append(")\n");
        }

        if (mission.getEconomicAssessment() != null) {
            EconomicAssessmentEntity ea = mission.getEconomicAssessment();
            sb.append("Economic assessment:\n");
            sb.append("  Total damage: ").append(ea.getTotalDamageCost()).append("\n");
            sb.append("  Infrastructure: ").append(ea.getInfrastructureDamage()).append("\n");
            sb.append("  Commercial: ").append(ea.getCommercialDamage()).append("\n");
            sb.append("  Transport: ").append(ea.getTransportDamage()).append("\n");
            sb.append("  Recovery days: ").append(ea.getRecoveryEstimateDays()).append("\n");
            sb.append("  Insurance covered: ").append(ea.getInsuranceCovered()).append("\n");
        }

        if (mission.getEnemyActivity() != null) {
            EnemyActivityEntity ea = mission.getEnemyActivity();
            sb.append("Enemy activity:\n");
            sb.append("  Behavior type: ").append(ea.getBehaviorType()).append("\n");
            sb.append("  Mobility: ").append(ea.getMobility()).append("\n");
            if (ea.getAttackPatterns() != null && !ea.getAttackPatterns().isEmpty()) {
                sb.append("  Attack patterns: ").append(String.join(", ", ea.getAttackPatterns())).append("\n");
            }
        }

        if (mission.getEnvironmentConditions() != null) {
            EnvironmentConditionsEntity ec = mission.getEnvironmentConditions();
            sb.append("Environment conditions: weather=").append(ec.getWeather())
              .append(", visibility=").append(ec.getVisibility())
              .append(", cursed energy density=").append(ec.getCursedEnergyDensity()).append("\n");
        }

        if (mission.getCivilianImpact() != null) {
            CivilianImpactEntity ci = mission.getCivilianImpact();
            sb.append("Civilian impact: evacuated=").append(ci.getEvacuated())
              .append(", injured=").append(ci.getInjured())
              .append(", missing=").append(ci.getMissing()).append("\n");
        }

        if (mission.getTimelineEvents() != null && !mission.getTimelineEvents().isEmpty()) {
            sb.append("Timeline:\n");
            for (TimelineEventEntity e : mission.getTimelineEvents()) {
                sb.append("  ").append(e.getTimestamp())
                  .append(" - ").append(e.getType())
                  .append(": ").append(e.getDescription()).append("\n");
            }
        }

        if (mission.getComment() != null) {
            sb.append("Comment: ").append(mission.getComment()).append("\n");
        }

        return sb.toString();
    }
}