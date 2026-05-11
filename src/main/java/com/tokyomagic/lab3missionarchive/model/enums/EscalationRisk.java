package com.tokyomagic.lab3missionarchive.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EscalationRisk {
    HIGH, MEDIUM, LOW, UNKNOWN;

    @JsonCreator
    public static EscalationRisk fromString(String value) {
        if (value == null) return UNKNOWN;
        try {
            return EscalationRisk.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }


}