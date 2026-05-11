package com.tokyomagic.lab3missionarchive.report;

import com.tokyomagic.lab3missionarchive.entity.MissionEntity;

public class SummaryReportGenerator implements ReportGenerator {
    @Override
    public String generate(MissionEntity mission) {
        StringBuilder sb = new StringBuilder();
        sb.append("~~~ Mission Summary Report ~~~\n");
        sb.append("ID: ").append(mission.getMissionId()).append("\n");
        sb.append("Date: ").append(mission.getDate()).append("\n");
        sb.append("Location: ").append(mission.getLocation()).append("\n");
        sb.append("Outcome: ").append(mission.getOutcome()).append("\n");
        if (mission.getCurse() != null) {
            sb.append("Curse: ").append(mission.getCurse().getName())
              .append(" (level ").append(mission.getCurse().getThreatLevel()).append(")\n");
        }
        sb.append("Participants: ").append(mission.getSorcerers().size()).append("\n");
        sb.append("Techniques used: ").append(mission.getTechniques().size()).append("\n");
        if (mission.getEconomicAssessment() != null) {
            sb.append("Economic damage: ").append(mission.getEconomicAssessment().getTotalDamageCost()).append("\n");
        }
        return sb.toString();
    }
}