package com.tokyomagic.lab3missionarchive.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SorcererRank {
    GRADE_1, GRADE_2, GRADE_3, GRADE_4, SEMI_GRADE_1, SPECIAL_GRADE, UNKNOWN;

    @JsonCreator
    public static SorcererRank fromString(String value) {
        if (value == null) return UNKNOWN;
        try {
            return SorcererRank.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}