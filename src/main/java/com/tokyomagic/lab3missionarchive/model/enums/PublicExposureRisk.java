package com.tokyomagic.lab3missionarchive.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PublicExposureRisk {
    HIGH, MEDIUM, LOW, UNKNOWN;

    @JsonCreator
    public static PublicExposureRisk fromString(String value) {
        if (value == null) return UNKNOWN;
        try {
            return PublicExposureRisk.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

}