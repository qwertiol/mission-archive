package com.tokyomagic.lab3missionarchive.controller;

import com.tokyomagic.lab3missionarchive.dto.MissionDetailDto;
import com.tokyomagic.lab3missionarchive.dto.MissionListItem;
import com.tokyomagic.lab3missionarchive.dto.MissionUpdateRequest;
import com.tokyomagic.lab3missionarchive.service.MissionService;
import com.tokyomagic.lab3missionarchive.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@Tag(name = "Mission Archive API", description = "Tokyo Magic College mission archive management")
public class MissionController {

    private final MissionService missionService;
    private final ReportService reportService;

    public MissionController(MissionService missionService, ReportService reportService) {
        this.missionService = missionService;
        this.reportService = reportService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload mission file", description = "Accepts a mission description file and saves it to the archive")
    public ResponseEntity<MissionDetailDto> uploadMission(@RequestParam("file") MultipartFile file) {
        MissionDetailDto saved = missionService.uploadMission(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    @Operation(summary = "List all missions", description = "Returns brief information about all missions in the archive")
    public ResponseEntity<List<MissionListItem>> getAllMissions() {
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get mission by ID", description = "Returns full mission details in JSON format")
    public ResponseEntity<MissionDetailDto> getMissionById(@PathVariable Long id) {
        return ResponseEntity.ok(missionService.getMissionById(id));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update mission", description = "Updates individual mission fields")
    public ResponseEntity<MissionDetailDto> updateMission(
            @PathVariable Long id,
            @RequestBody MissionUpdateRequest updateRequest) {
        return ResponseEntity.ok(missionService.updateMission(id, updateRequest));
    }

    @GetMapping(value = "/{id}/report/summary", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Summary report for mission (text)", description = "Returns a brief mission report in plain text")
    public ResponseEntity<String> getSummaryReport(@PathVariable Long id) {
        String report = reportService.generateSummary(id);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(report);
    }

    @GetMapping(value = "/{id}/report/detailed", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Detailed report for mission (text)", description = "Returns a detailed mission report in plain text")
    public ResponseEntity<String> getDetailedReport(@PathVariable Long id) {
        String report = reportService.generateDetailed(id);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(report);
    }
}