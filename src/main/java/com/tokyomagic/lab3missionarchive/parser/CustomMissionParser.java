package com.tokyomagic.lab3missionarchive.parser;

import com.tokyomagic.lab3missionarchive.builder.MissionBuilder;
import com.tokyomagic.lab3missionarchive.model.*;
import com.tokyomagic.lab3missionarchive.model.enums.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class CustomMissionParser implements MissionParser {
    @Override
    public Mission parse(File file) throws IOException {
        MissionBuilder builder = new MissionBuilder();
        List<Sorcerer> sorcerers = new ArrayList<>();
        List<Technique> techniques = new ArrayList<>();
        List<TimelineEvent> timeline = new ArrayList<>();
        CivilianImpact civilianImpact = null;
        EnemyActivity enemyActivity = null;
        String outcomeStr = null;
        Long damageCost = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length == 0) continue;
                String type = parts[0];
                switch (type) {
                    case "MISSION_CREATED":
                        if (parts.length >= 4) {
                            builder.setMissionId(parts[1]);
                            builder.setDate(LocalDate.parse(parts[2]));
                            builder.setLocation(parts[3]);
                        }
                        break;
                    case "CURSE_DETECTED":
                        if (parts.length >= 3) {
                            Curse curse = new Curse();
                            curse.setName(parts[1]);
                            curse.setThreatLevelString(parts[2]);
                            builder.setCurse(curse);
                        }
                        break;
                    case "SORCERER_ASSIGNED":
                        if (parts.length >= 3) {
                            Sorcerer s = new Sorcerer();
                            s.setName(parts[1]);
                            s.setRankString(parts[2]);
                            sorcerers.add(s);
                        }
                        break;
                    case "TECHNIQUE_USED":
                        if (parts.length >= 5) {
                            Technique t = new Technique();
                            t.setName(parts[1]);
                            t.setTypeString(parts[2]);
                            t.setOwner(parts[3]);
                            t.setDamage(Long.parseLong(parts[4]));
                            techniques.add(t);
                        }
                        break;
                    case "TIMELINE_EVENT":
                        if (parts.length >= 4) {
                            timeline.add(new TimelineEvent(LocalDateTime.parse(parts[1]), parts[2], parts[3]));
                        }
                        break;
                    case "ENEMY_ACTION":
                        if (parts.length >= 3) {
                            if (enemyActivity == null) enemyActivity = new EnemyActivity();
                            if (enemyActivity.getAttackPatterns() == null)
                                enemyActivity.setAttackPatterns(new ArrayList<>());
                            enemyActivity.getAttackPatterns().add(parts[1] + ": " + parts[2]);
                        }
                        break;
                    case "CIVILIAN_IMPACT":
                        civilianImpact = new CivilianImpact();
                        for (int i = 1; i < parts.length; i++) {
                            String[] kv = parts[i].split("=");
                            if (kv.length == 2) {
                                switch (kv[0]) {
                                    case "evacuated": civilianImpact.setEvacuated(Integer.parseInt(kv[1])); break;
                                    case "injured": civilianImpact.setInjured(Integer.parseInt(kv[1])); break;
                                    case "missing": civilianImpact.setMissing(Integer.parseInt(kv[1])); break;
                                    case "publicExposureRisk": civilianImpact.setPublicExposureRiskString(kv[1]); break;
                                }
                            }
                        }
                        break;
                    case "MISSION_RESULT":
                        if (parts.length >= 2) {
                            outcomeStr = parts[1];
                            if (parts.length >= 3 && parts[2].startsWith("damageCost=")) {
                                damageCost = Long.parseLong(parts[2].substring("damageCost=".length()));
                            }
                        }
                        break;
                }
            }
        }

        builder.setSorcerers(sorcerers);
        builder.setTechniques(techniques);
        builder.setTimelineEvents(timeline);
        if (civilianImpact != null) builder.setCivilianImpact(civilianImpact);
        if (enemyActivity != null) builder.setEnemyActivity(enemyActivity);
        if (outcomeStr != null) builder.setOutcomeString(outcomeStr);
        if (damageCost != null) builder.setDamageCost(damageCost);
        return builder.build();
    }
}