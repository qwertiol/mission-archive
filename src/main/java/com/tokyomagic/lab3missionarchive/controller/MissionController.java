package com.tokyomagic.lab3missionarchive.controller;

import com.tokyomagic.lab3missionarchive.dto.MissionDetailDto;
import com.tokyomagic.lab3missionarchive.dto.MissionListItem;
import com.tokyomagic.lab3missionarchive.dto.MissionUpdateRequest;
import com.tokyomagic.lab3missionarchive.service.MissionService;
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
@Tag(name = "Mission Archive API", description = "Управление архивом миссий Токийского магического колледжа")
public class MissionController {

    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить файл миссии", description = "Принимает файл с описанием миссии и сохраняет в архив")
    public ResponseEntity<MissionDetailDto> uploadMission(@RequestParam("file") MultipartFile file) {
        MissionDetailDto saved = missionService.uploadMission(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    @Operation(summary = "Список всех миссий", description = "Возвращает краткую информацию обо всех миссиях в архиве")
    public ResponseEntity<List<MissionListItem>> getAllMissions() {
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить миссию по ID", description = "Возвращает полную информацию о миссии в формате JSON")
    public ResponseEntity<MissionDetailDto> getMissionById(@PathVariable Long id) {
        return ResponseEntity.ok(missionService.getMissionById(id));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Частичное обновление миссии", description = "Обновляет отдельные поля миссии")
    public ResponseEntity<MissionDetailDto> updateMission(
            @PathVariable Long id,
            @RequestBody MissionUpdateRequest updateRequest) {
        return ResponseEntity.ok(missionService.updateMission(id, updateRequest));
    }

    @GetMapping(value = "/{id}/report/summary", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Краткий отчёт по миссии (текст)", description = "Возвращает краткий отчёт в текстовом формате")
    public ResponseEntity<String> getSummaryReport(@PathVariable Long id) {
        String report = missionService.generateSummaryReport(id);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(report);
    }

    @GetMapping(value = "/{id}/report/detailed", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Детальный отчёт по миссии (текст)", description = "Возвращает детальный отчёт в текстовом формате")
    public ResponseEntity<String> getDetailedReport(@PathVariable Long id) {
        String report = missionService.generateDetailedReport(id);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(report);
    }
}