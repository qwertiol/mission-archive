package com.tokyomagic.lab3missionarchive.service;

import com.tokyomagic.lab3missionarchive.dto.*;
import com.tokyomagic.lab3missionarchive.entity.*;
import com.tokyomagic.lab3missionarchive.model.*;
import com.tokyomagic.lab3missionarchive.model.enums.*;
import com.tokyomagic.lab3missionarchive.parser.*;
import com.tokyomagic.lab3missionarchive.repository.MissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final ParserRegistry parserRegistry;

    public MissionServiceImpl(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
        parserRegistry = new ParserRegistry();
        parserRegistry.registerParser(f -> f.getName().toLowerCase().endsWith(".json"), new JsonMissionParser());
        parserRegistry.registerParser(f -> f.getName().toLowerCase().endsWith(".yaml") || f.getName().toLowerCase().endsWith(".yml"), new YamlMissionParser());
        parserRegistry.registerParser(f -> f.getName().toLowerCase().endsWith(".xml"), new XmlMissionParser());
        parserRegistry.registerParser(f -> f.getName().toLowerCase().endsWith(".txt"), new TxtMissionParser());
    }

    @Override
    @Transactional
    public MissionDetailDto uploadMission(MultipartFile multipartFile) {
        Path tempFile;
        try {
            tempFile = Files.createTempFile("mission_", "_" + multipartFile.getOriginalFilename());
            multipartFile.transferTo(tempFile.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить файл", e);
        }

        File file = tempFile.toFile();
        try {
            MissionParser parser = parserRegistry.getParser(file);
            Mission missionPojo = parser.parse(file);

            Optional<MissionEntity> existing = missionRepository.findByMissionId(missionPojo.getMissionId());
            MissionEntity missionEntity;
            if (existing.isPresent()) {
                missionEntity = existing.get();
                updateEntityFromPojo(missionEntity, missionPojo);
            } else {
                missionEntity = convertToEntity(missionPojo);
            }

            MissionEntity saved = missionRepository.save(missionEntity);
            return toDetailDto(saved);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке миссии: " + e.getMessage(), e);
        } finally {
            file.delete();
        }
    }

    @Override
    public List<MissionListItem> getAllMissions() {
        return missionRepository.findAll().stream()
                .map(this::toListItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public MissionDetailDto getMissionById(Long id) {
        MissionEntity entity = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Миссия с id " + id + " не найдена"));
        return toDetailDto(entity);
    }

    @Override
    @Transactional
    public MissionDetailDto updateMission(Long id, MissionUpdateRequest updateRequest) {
        MissionEntity mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Миссия с id " + id + " не найдена"));

        if (updateRequest.getLocation() != null) mission.setLocation(updateRequest.getLocation());
        if (updateRequest.getOutcome() != null) mission.setOutcome(updateRequest.getOutcome());
        if (updateRequest.getDamageCost() != null) mission.setDamageCost(updateRequest.getDamageCost());
        if (updateRequest.getComment() != null) mission.setComment(updateRequest.getComment());

        MissionEntity updated = missionRepository.save(mission);
        return toDetailDto(updated);
    }

    @Override
    public String generateSummaryReport(Long missionId) {
        MissionEntity m = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Миссия с id " + missionId + " не найдена"));
        StringBuilder sb = new StringBuilder();
        sb.append("~~~ Краткий отчет о миссии ~~~\n");
        sb.append("ID: ").append(m.getMissionId()).append("\n");
        sb.append("Дата: ").append(m.getDate()).append("\n");
        sb.append("Локация: ").append(m.getLocation()).append("\n");
        sb.append("Исход: ").append(m.getOutcome()).append("\n");
        if (m.getCurse() != null) {
            sb.append("Проклятие: ").append(m.getCurse().getName())
              .append(" (уровень ").append(m.getCurse().getThreatLevel()).append(")\n");
        }
        sb.append("Участников: ").append(m.getSorcerers().size()).append("\n");
        sb.append("Техник применено: ").append(m.getTechniques().size()).append("\n");
        if (m.getEconomicAssessment() != null) {
            sb.append("Экономический ущерб: ").append(m.getEconomicAssessment().getTotalDamageCost()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String generateDetailedReport(Long missionId) {
        MissionEntity m = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Миссия с id " + missionId + " не найдена"));
        StringBuilder sb = new StringBuilder();
        sb.append("~~~ Детальный отчет о миссии ~~~\n");
        sb.append("ID миссии: ").append(m.getMissionId()).append("\n");
        sb.append("Дата: ").append(m.getDate()).append("\n");
        sb.append("Место: ").append(m.getLocation()).append("\n");
        sb.append("Исход: ").append(m.getOutcome()).append("\n");
        sb.append("Ущерб: ").append(m.getDamageCost()).append("\n");

        if (m.getCurse() != null) {
            sb.append("Проклятие: ").append(m.getCurse().getName())
              .append(" (уровень: ").append(m.getCurse().getThreatLevel()).append(")\n");
        }

        sb.append("Участники:\n");
        for (SorcererEntity s : m.getSorcerers()) {
            sb.append("  - ").append(s.getName())
              .append(" (ранг: ").append(s.getRank()).append(")\n");
        }

        sb.append("Примененные техники:\n");
        for (TechniqueEntity t : m.getTechniques()) {
            sb.append("  - ").append(t.getName())
              .append(" (тип: ").append(t.getType())
              .append(", владелец: ").append(t.getOwner())
              .append(", урон: ").append(t.getDamage()).append(")\n");
        }

        if (m.getEconomicAssessment() != null) {
            EconomicAssessmentEntity ea = m.getEconomicAssessment();
            sb.append("Экономическая оценка:\n");
            sb.append("  Общий ущерб: ").append(ea.getTotalDamageCost()).append("\n");
            sb.append("  Инфраструктура: ").append(ea.getInfrastructureDamage()).append("\n");
            sb.append("  Коммерческий: ").append(ea.getCommercialDamage()).append("\n");
            sb.append("  Транспорт: ").append(ea.getTransportDamage()).append("\n");
            sb.append("  Дни восстановления: ").append(ea.getRecoveryEstimateDays()).append("\n");
            sb.append("  Страховое покрытие: ").append(ea.getInsuranceCovered()).append("\n");
        }

        if (m.getEnemyActivity() != null) {
            EnemyActivityEntity ea = m.getEnemyActivity();
            sb.append("Активность врага:\n");
            sb.append("  Тип поведения: ").append(ea.getBehaviorType()).append("\n");
            sb.append("  Мобильность: ").append(ea.getMobility()).append("\n");
            if (ea.getAttackPatterns() != null && !ea.getAttackPatterns().isEmpty()) {
                sb.append("  Паттерны атак: ").append(String.join(", ", ea.getAttackPatterns())).append("\n");
            }
        }

        if (m.getEnvironmentConditions() != null) {
            EnvironmentConditionsEntity ec = m.getEnvironmentConditions();
            sb.append("Условия среды: погода=").append(ec.getWeather())
              .append(", видимость=").append(ec.getVisibility())
              .append(", плотность энергии=").append(ec.getCursedEnergyDensity()).append("\n");
        }

        if (m.getCivilianImpact() != null) {
            CivilianImpactEntity ci = m.getCivilianImpact();
            sb.append("Влияние на гражданских: эвакуировано=").append(ci.getEvacuated())
              .append(", пострадало=").append(ci.getInjured())
              .append(", пропало=").append(ci.getMissing()).append("\n");
        }

        if (m.getTimelineEvents() != null && !m.getTimelineEvents().isEmpty()) {
            sb.append("Хронология:\n");
            for (TimelineEventEntity e : m.getTimelineEvents()) {
                sb.append("  ").append(e.getTimestamp())
                  .append(" - ").append(e.getType())
                  .append(": ").append(e.getDescription()).append("\n");
            }
        }

        if (m.getComment() != null) {
            sb.append("Комментарий: ").append(m.getComment()).append("\n");
        }

        return sb.toString();
    }

    private MissionListItem toListItemDto(MissionEntity entity) {
        return new MissionListItem(
                entity.getId(),
                entity.getMissionId(),
                entity.getDate() != null ? entity.getDate().toString() : "",
                entity.getLocation(),
                entity.getOutcome() != null ? entity.getOutcome().name() : ""
        );
    }

    private MissionDetailDto toDetailDto(MissionEntity entity) {
        MissionDetailDto dto = new MissionDetailDto();
        dto.setId(entity.getId());
        dto.setMissionId(entity.getMissionId());
        dto.setDate(entity.getDate() != null ? entity.getDate().toString() : "");
        dto.setLocation(entity.getLocation());
        dto.setOutcome(entity.getOutcome() != null ? entity.getOutcome().name() : "");
        dto.setDamageCost(entity.getDamageCost());
        dto.setComment(entity.getComment());

        if (entity.getCurse() != null) {
            dto.setCurse(new MissionDetailDto.CurseDto(
                    entity.getCurse().getName(),
                    entity.getCurse().getThreatLevel() != null ? entity.getCurse().getThreatLevel().name() : ""
            ));
        }
        if (entity.getEconomicAssessment() != null) {
            EconomicAssessmentEntity ea = entity.getEconomicAssessment();
            MissionDetailDto.EconomicAssessmentDto eaDto = new MissionDetailDto.EconomicAssessmentDto();
            eaDto.setTotalDamageCost(ea.getTotalDamageCost());
            eaDto.setInfrastructureDamage(ea.getInfrastructureDamage());
            eaDto.setCommercialDamage(ea.getCommercialDamage());
            eaDto.setTransportDamage(ea.getTransportDamage());
            eaDto.setRecoveryEstimateDays(ea.getRecoveryEstimateDays());
            eaDto.setInsuranceCovered(ea.getInsuranceCovered());
            dto.setEconomicAssessment(eaDto);
        }
        if (entity.getEnemyActivity() != null) {
            EnemyActivityEntity ea = entity.getEnemyActivity();
            MissionDetailDto.EnemyActivityDto eaDto = new MissionDetailDto.EnemyActivityDto();
            eaDto.setBehaviorType(ea.getBehaviorType());
            eaDto.setTargetPriority(ea.getTargetPriority());
            eaDto.setMobility(ea.getMobility() != null ? ea.getMobility().name() : "");
            eaDto.setEscalationRisk(ea.getEscalationRisk() != null ? ea.getEscalationRisk().name() : "");
            eaDto.setAttackPatterns(ea.getAttackPatterns());
            eaDto.setCountermeasuresUsed(ea.getCountermeasuresUsed());
            dto.setEnemyActivity(eaDto);
        }
        if (entity.getEnvironmentConditions() != null) {
            EnvironmentConditionsEntity ec = entity.getEnvironmentConditions();
            MissionDetailDto.EnvironmentConditionsDto ecDto = new MissionDetailDto.EnvironmentConditionsDto();
            ecDto.setWeather(ec.getWeather());
            ecDto.setTimeOfDay(ec.getTimeOfDay());
            ecDto.setVisibility(ec.getVisibility() != null ? ec.getVisibility().name() : "");
            ecDto.setCursedEnergyDensity(ec.getCursedEnergyDensity());
            dto.setEnvironmentConditions(ecDto);
        }
        if (entity.getCivilianImpact() != null) {
            CivilianImpactEntity ci = entity.getCivilianImpact();
            MissionDetailDto.CivilianImpactDto ciDto = new MissionDetailDto.CivilianImpactDto();
            ciDto.setEvacuated(ci.getEvacuated());
            ciDto.setInjured(ci.getInjured());
            ciDto.setMissing(ci.getMissing());
            ciDto.setPublicExposureRisk(ci.getPublicExposureRisk() != null ? ci.getPublicExposureRisk().name() : "");
            dto.setCivilianImpact(ciDto);
        }

        dto.setSorcerers(entity.getSorcerers().stream()
                .map(s -> new MissionDetailDto.SorcererDto(s.getName(), s.getRank() != null ? s.getRank().name() : ""))
                .collect(Collectors.toList()));
        dto.setTechniques(entity.getTechniques().stream()
                .map(t -> new MissionDetailDto.TechniqueDto(t.getName(), t.getType() != null ? t.getType().name() : "",
                        t.getOwner(), t.getDamage()))
                .collect(Collectors.toList()));
        dto.setTimelineEvents(entity.getTimelineEvents().stream()
                .map(te -> new MissionDetailDto.TimelineEventDto(
                        te.getTimestamp() != null ? te.getTimestamp().toString() : "",
                        te.getType(),
                        te.getDescription()))
                .collect(Collectors.toList()));

        return dto;
    }

    private MissionEntity convertToEntity(Mission pojo) {
        MissionEntity entity = new MissionEntity();
        entity.setMissionId(pojo.getMissionId());
        entity.setDate(pojo.getDate());
        entity.setLocation(pojo.getLocation());
        entity.setOutcome(pojo.getOutcome());
        entity.setDamageCost(pojo.getDamageCost());
        entity.setComment(pojo.getComment());

        if (pojo.getCurse() != null) {
            entity.setCurse(convertCurse(pojo.getCurse()));
        }
        if (pojo.getEconomicAssessment() != null) {
            entity.setEconomicAssessment(convertEconomicAssessment(pojo.getEconomicAssessment()));
        }
        if (pojo.getEnemyActivity() != null) {
            entity.setEnemyActivity(convertEnemyActivity(pojo.getEnemyActivity()));
        }
        if (pojo.getEnvironmentConditions() != null) {
            entity.setEnvironmentConditions(convertEnvironmentConditions(pojo.getEnvironmentConditions()));
        }
        if (pojo.getCivilianImpact() != null) {
            entity.setCivilianImpact(convertCivilianImpact(pojo.getCivilianImpact()));
        }

        if (pojo.getSorcerers() != null) {
            pojo.getSorcerers().forEach(s -> entity.addSorcerer(convertSorcerer(s)));
        }
        if (pojo.getTechniques() != null) {
            pojo.getTechniques().forEach(t -> entity.addTechnique(convertTechnique(t)));
        }
        if (pojo.getTimelineEvents() != null) {
            pojo.getTimelineEvents().forEach(e -> entity.addTimelineEvent(convertTimelineEvent(e)));
        }
        return entity;
    }

    private void updateEntityFromPojo(MissionEntity entity, Mission pojo) {
        entity.setDate(pojo.getDate());
        entity.setLocation(pojo.getLocation());
        entity.setOutcome(pojo.getOutcome());
        entity.setDamageCost(pojo.getDamageCost());
        entity.setComment(pojo.getComment());

        if (pojo.getCurse() != null) {
            entity.setCurse(convertCurse(pojo.getCurse()));
        }
        if (pojo.getEconomicAssessment() != null) {
            entity.setEconomicAssessment(convertEconomicAssessment(pojo.getEconomicAssessment()));
        }
        if (pojo.getEnemyActivity() != null) {
            entity.setEnemyActivity(convertEnemyActivity(pojo.getEnemyActivity()));
        }
        if (pojo.getEnvironmentConditions() != null) {
            entity.setEnvironmentConditions(convertEnvironmentConditions(pojo.getEnvironmentConditions()));
        }
        if (pojo.getCivilianImpact() != null) {
            entity.setCivilianImpact(convertCivilianImpact(pojo.getCivilianImpact()));
        }

        entity.getSorcerers().clear();
        if (pojo.getSorcerers() != null) {
            pojo.getSorcerers().forEach(s -> entity.addSorcerer(convertSorcerer(s)));
        }
        entity.getTechniques().clear();
        if (pojo.getTechniques() != null) {
            pojo.getTechniques().forEach(t -> entity.addTechnique(convertTechnique(t)));
        }
        entity.getTimelineEvents().clear();
        if (pojo.getTimelineEvents() != null) {
            pojo.getTimelineEvents().forEach(e -> entity.addTimelineEvent(convertTimelineEvent(e)));
        }
    }

    private CurseEntity convertCurse(Curse curse) {
        CurseEntity e = new CurseEntity();
        e.setName(curse.getName());
        e.setThreatLevel(curse.getThreatLevel());
        return e;
    }

    private SorcererEntity convertSorcerer(Sorcerer sorcerer) {
        SorcererEntity e = new SorcererEntity();
        e.setName(sorcerer.getName());
        e.setRank(sorcerer.getRank());
        return e;
    }

    private TechniqueEntity convertTechnique(Technique technique) {
        TechniqueEntity e = new TechniqueEntity();
        e.setName(technique.getName());
        e.setType(technique.getType());
        e.setOwner(technique.getOwner());
        e.setDamage(technique.getDamage());
        return e;
    }

    private TimelineEventEntity convertTimelineEvent(TimelineEvent event) {
        TimelineEventEntity e = new TimelineEventEntity();
        e.setTimestamp(event.getTimestamp());
        e.setType(event.getType());
        e.setDescription(event.getDescription());
        return e;
    }

    private EconomicAssessmentEntity convertEconomicAssessment(EconomicAssessment ea) {
        EconomicAssessmentEntity e = new EconomicAssessmentEntity();
        e.setTotalDamageCost(ea.getTotalDamageCost());
        e.setInfrastructureDamage(ea.getInfrastructureDamage());
        e.setCommercialDamage(ea.getCommercialDamage());
        e.setTransportDamage(ea.getTransportDamage());
        e.setRecoveryEstimateDays(ea.getRecoveryEstimateDays());
        e.setInsuranceCovered(ea.getInsuranceCovered());
        return e;
    }

    private EnemyActivityEntity convertEnemyActivity(EnemyActivity ea) {
        EnemyActivityEntity e = new EnemyActivityEntity();
        e.setBehaviorType(ea.getBehaviorType());
        e.setTargetPriority(ea.getTargetPriority());
        e.setMobility(ea.getMobility());
        e.setEscalationRisk(ea.getEscalationRisk());
        e.setAttackPatterns(ea.getAttackPatterns() != null ? new ArrayList<>(ea.getAttackPatterns()) : new ArrayList<>());
        e.setCountermeasuresUsed(ea.getCountermeasuresUsed() != null ? new ArrayList<>(ea.getCountermeasuresUsed()) : new ArrayList<>());
        return e;
    }

    private EnvironmentConditionsEntity convertEnvironmentConditions(EnvironmentConditions ec) {
        EnvironmentConditionsEntity e = new EnvironmentConditionsEntity();
        e.setWeather(ec.getWeather());
        e.setTimeOfDay(ec.getTimeOfDay());
        e.setVisibility(ec.getVisibility());
        e.setCursedEnergyDensity(ec.getCursedEnergyDensity());
        return e;
    }

    private CivilianImpactEntity convertCivilianImpact(CivilianImpact ci) {
        CivilianImpactEntity e = new CivilianImpactEntity();
        e.setEvacuated(ci.getEvacuated());
        e.setInjured(ci.getInjured());
        e.setMissing(ci.getMissing());
        e.setPublicExposureRisk(ci.getPublicExposureRisk());
        return e;
    }
}