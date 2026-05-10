package com.tokyomagic.lab3missionarchive.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ParserRegistry {
    private final Map<Predicate<File>, MissionParser> parsers = new HashMap<>();

    public void registerParser(Predicate<File> condition, MissionParser parser) {
        parsers.put(condition, parser);
    }

    public MissionParser getParser(File file) {
        for (var entry : parsers.entrySet()) {
            if (entry.getKey().test(file)) {
                return entry.getValue();
            }
        }
        // fallback: если ни один парсер не подошёл, используем универсальный CustomMissionParser
        return new CustomMissionParser();
    }
}