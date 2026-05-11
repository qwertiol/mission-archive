package com.tokyomagic.lab3missionarchive.report;

import com.tokyomagic.lab3missionarchive.entity.MissionEntity;

public interface ReportGenerator {
    String generate(MissionEntity mission);
}