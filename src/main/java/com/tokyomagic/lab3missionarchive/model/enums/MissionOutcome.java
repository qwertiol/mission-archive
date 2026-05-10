package com.tokyomagic.lab3missionarchive.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MissionOutcome {
    SUCCESS, PARTIAL_SUCCESS, FAILURE, UNKNOWN;

    @JsonCreator // Указывает Jackson, что этот метод нужно использовать для создания объекта enum из строки, которая пришла во входном файле
    public static MissionOutcome fromString(String value) {
        if (value == null) return UNKNOWN;
        try {
            return MissionOutcome.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

}