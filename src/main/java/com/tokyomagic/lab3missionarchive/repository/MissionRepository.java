package com.tokyomagic.lab3missionarchive.repository;

import com.tokyomagic.lab3missionarchive.entity.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<MissionEntity, Long> {
    Optional<MissionEntity> findByMissionId(String missionId);
    boolean existsByMissionId(String missionId);
}
