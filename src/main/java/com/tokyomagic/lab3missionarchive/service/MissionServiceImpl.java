package com.tokyomagic.lab3missionarchive.service;

import com.tokyomagic.lab3missionarchive.dto.*;
import com.tokyomagic.lab3missionarchive.entity.*;
import com.tokyomagic.lab3missionarchive.model.*;
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
            throw new RuntimeException("Failed to upload file", e);
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
            throw new RuntimeException("Error processing mission: " + e.getMessage(), e);
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
                .orElseThrow(() -> new RuntimeException("Mission with id " + id + " not found"));
        return toDetailDto(entity);
    }

    @Override
    @Transactional
    public MissionDetailDto updateMission(Long id, MissionUpdateRequest updateRequest) {
        MissionEntity mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission with id " + id + " not found"));

        if (updateRequest.getLocation() != null) mission.setLocation(updateRequest.getLocation());
        if (updateRequest.getOutcome() != null) mission.setOutcome(updateRequest.getOutcome());
        if (updateRequest.getDamageCost() != null) mission.setDamageCost(updateRequest.getDamageCost());
        if (updateRequest.getComment() != null) mission.setComment(updateRequest.getComment());

        MissionEntity updated = missionRepository.save(mission);
        return toDetailDto(updated);
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