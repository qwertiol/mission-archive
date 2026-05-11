package com.tokyomagic.lab3missionarchive.service;

import com.tokyomagic.lab3missionarchive.dto.MissionDetailDto;
import com.tokyomagic.lab3missionarchive.dto.MissionListItem;
import com.tokyomagic.lab3missionarchive.dto.MissionUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MissionService {
    MissionDetailDto uploadMission(MultipartFile file);
    List<MissionListItem> getAllMissions();
    MissionDetailDto getMissionById(Long id);
    MissionDetailDto updateMission(Long id, MissionUpdateRequest updateRequest);
}