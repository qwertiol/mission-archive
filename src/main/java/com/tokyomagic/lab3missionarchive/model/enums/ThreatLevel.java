package com.tokyomagic.lab3missionarchive.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ThreatLevel {
    HIGH, SPECIAL_GRADE, MEDIUM, LOW, UNKNOWN;

    @JsonCreator
    public static ThreatLevel fromString(String value) {
        if (value == null) return UNKNOWN;
        try {
            return ThreatLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

}