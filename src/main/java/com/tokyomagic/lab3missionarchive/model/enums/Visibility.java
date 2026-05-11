package com.tokyomagic.lab3missionarchive.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Visibility {
    HIGH, MEDIUM, LOW, UNKNOWN;

    @JsonCreator
    public static Visibility fromString(String value) {
        if (value == null) return UNKNOWN;
        try {
            return Visibility.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

}