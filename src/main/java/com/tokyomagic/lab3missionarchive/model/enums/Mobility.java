package com.tokyomagic.lab3missionarchive.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Mobility {
    HIGH, MEDIUM, LOW, UNKNOWN;

    @JsonCreator
    public static Mobility fromString(String value) {
        if (value == null) return UNKNOWN;
        try {
            return Mobility.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

}