package com.tokyomagic.lab3missionarchive.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TechniqueType {
    INNATE, SHIKIGAMI, WEAPON, BODY, UNKNOWN;

    @JsonCreator
    public static TechniqueType fromString(String value) {
        if (value == null) return UNKNOWN;
        try {
            return TechniqueType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

}