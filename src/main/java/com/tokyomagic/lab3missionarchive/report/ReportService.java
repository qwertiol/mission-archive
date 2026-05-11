package com.tokyomagic.lab3missionarchive.service;

import com.tokyomagic.lab3missionarchive.entity.MissionEntity;
import com.tokyomagic.lab3missionarchive.report.DetailedReportGenerator;
import com.tokyomagic.lab3missionarchive.report.ReportGenerator;
import com.tokyomagic.lab3missionarchive.report.SummaryReportGenerator;
import com.tokyomagic.lab3missionarchive.repository.MissionRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final MissionRepository missionRepository;
    private final ReportGenerator summaryGenerator;
    private final ReportGenerator detailedGenerator;

    public ReportService(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
        this.summaryGenerator = new SummaryReportGenerator();
        this.detailedGenerator = new DetailedReportGenerator();
    }

    public String generateSummary(Long missionId) {
        MissionEntity mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found"));
        return summaryGenerator.generate(mission);
    }

    public String generateDetailed(Long missionId) {
        MissionEntity mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found"));
        return detailedGenerator.generate(mission);
    }
}