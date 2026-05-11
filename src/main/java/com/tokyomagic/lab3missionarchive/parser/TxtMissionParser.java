package com.tokyomagic.lab3missionarchive.parser;

import com.tokyomagic.lab3missionarchive.builder.MissionBuilder;
import com.tokyomagic.lab3missionarchive.model.*;
import com.tokyomagic.lab3missionarchive.model.enums.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class TxtMissionParser implements MissionParser {

    @Override
    public Mission parse(File file) throws IOException {
        // Сначала читаем все строки, чтобы определить формат
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        // Определяем формат по первой значащей строке
        String firstNonEmpty = null;
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                firstNonEmpty = trimmed;
                break;
            }
        }

        if (firstNonEmpty == null) {
            throw new IOException("Файл пуст");
        }

        // Если строка содержит квадратные скобки то это новый формат
        if (firstNonEmpty.contains("[") && firstNonEmpty.contains("]")) {
            return parseSectionFormat(lines);
        } else {
            return parseFlatFormat(lines); // иначе стары формат
        }
    }

    /**
     * Разбор нового формата (секции в квадратных скобках и key=value).
     */
    private Mission parseSectionFormat(List<String> lines) throws IOException {
        MissionBuilder builder = new MissionBuilder();
        List<Sorcerer> sorcerers = new ArrayList<>();
        List<Technique> techniques = new ArrayList<>();
        EnvironmentConditions env = new EnvironmentConditions();
        Curse curse = null;
        String currentSection = null;
        Sorcerer currentSorcerer = null;
        Technique currentTechnique = null;

        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length() - 1);
                continue;
            }

            int eq = line.indexOf('=');
            if (eq == -1) continue;
            String key = line.substring(0, eq).trim();
            String value = line.substring(eq + 1).trim();

            if (currentSection == null || currentSection.equals("MISSION")) {
                switch (key) {
                    case "missionId": builder.setMissionId(value); break;
                    case "date": builder.setDate(LocalDate.parse(value)); break;
                    case "location": builder.setLocation(value); break;
                    case "outcome": builder.setOutcomeString(value); break;
                    case "damageCost": builder.setDamageCost(Long.parseLong(value)); break;
                }
            } else if (currentSection.equals("CURSE")) {
                if (curse == null) curse = new Curse();
                if (key.equals("name")) curse.setName(value);
                else if (key.equals("threatLevel")) curse.setThreatLevelString(value);
            } else if (currentSection.equals("SORCERER")) {
                if (currentSorcerer == null) {
                    currentSorcerer = new Sorcerer();
                    sorcerers.add(currentSorcerer);
                }
                if (key.equals("name")) currentSorcerer.setName(value);
                else if (key.equals("rank")) currentSorcerer.setRankString(value);
                if (currentSorcerer.getName() != null && currentSorcerer.getRank() != null) {
                    currentSorcerer = null;
                }
            } else if (currentSection.equals("TECHNIQUE")) {
                if (currentTechnique == null) {
                    currentTechnique = new Technique();
                    techniques.add(currentTechnique);
                }
                switch (key) {
                    case "name": currentTechnique.setName(value); break;
                    case "type": currentTechnique.setTypeString(value); break;
                    case "owner": currentTechnique.setOwner(value); break;
                    case "damage": currentTechnique.setDamage(Long.parseLong(value)); break;
                }
                if (currentTechnique.getName() != null && currentTechnique.getType() != null &&
                    currentTechnique.getOwner() != null && currentTechnique.getDamage() != 0) {
                    currentTechnique = null;
                }
            } else if (currentSection.equals("ENVIRONMENT")) {
                switch (key) {
                    case "weather": env.setWeather(value); break;
                    case "timeOfDay": env.setTimeOfDay(value); break;
                    case "visibility": env.setVisibilityString(value); break;
                    case "cursedEnergyDensity": env.setCursedEnergyDensity(Integer.parseInt(value)); break;
                }
            }
        }

        if (curse != null) builder.setCurse(curse);
        builder.setSorcerers(sorcerers);
        builder.setTechniques(techniques);
        if (env.getWeather() != null || env.getTimeOfDay() != null ||
            env.getVisibility() != null || env.getCursedEnergyDensity() != null) {
            builder.setEnvironmentConditions(env);
        }
        return builder.build();
    }

    /**
     * Разбор старого формата (двоеточия и индексы в квадратных скобках)
     */
    private Mission parseFlatFormat(List<String> lines) throws IOException {
        MissionBuilder builder = new MissionBuilder();
        Map<Integer, Sorcerer> sorcererMap = new HashMap<>();
        Map<Integer, Technique> techniqueMap = new HashMap<>();
        Curse curse = null;

        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) continue;

            int colon = line.indexOf(':');
            if (colon == -1) continue;
            String key = line.substring(0, colon).trim();
            String value = line.substring(colon + 1).trim();

            // Обработка основных полей миссии
            switch (key) {
                case "missionId":
                    builder.setMissionId(value);
                    break;
                case "date":
                    builder.setDate(LocalDate.parse(value));
                    break;
                case "location":
                    builder.setLocation(value);
                    break;
                case "outcome":
                    builder.setOutcomeString(value);
                    break;
                case "damageCost":
                    builder.setDamageCost(Long.parseLong(value));
                    break;
                default:
                    // Поле может быть вложенным: curse.name, sorcerer[0].name и т.д.
                    if (key.startsWith("curse.")) {
                        if (curse == null) curse = new Curse();
                        String subKey = key.substring("curse.".length());
                        if (subKey.equals("name")) {
                            curse.setName(value);
                        } else if (subKey.equals("threatLevel")) {
                            curse.setThreatLevelString(value);
                        }
                    } else if (key.startsWith("sorcerer[")) {
                        // Извлекаем индекс: sorcerer[0].name -> индекс 0, поле name
                        int bracketOpen = key.indexOf('[');
                        int bracketClose = key.indexOf(']');
                        if (bracketOpen != -1 && bracketClose != -1 && bracketClose > bracketOpen) {
                            String indexStr = key.substring(bracketOpen + 1, bracketClose);
                            try {
                                int index = Integer.parseInt(indexStr);
                                String field = key.substring(bracketClose + 1); // убираем ']'
                                if (field.startsWith(".")) field = field.substring(1); // убираем точку

                                Sorcerer sorcerer = sorcererMap.computeIfAbsent(index, k -> new Sorcerer());
                                if (field.equals("name")) {
                                    sorcerer.setName(value);
                                } else if (field.equals("rank")) {
                                    sorcerer.setRankString(value);
                                }
                            } catch (NumberFormatException ignored) {
                                // игнорируем некорректный индекс
                            }
                        }
                    } else if (key.startsWith("technique[")) {
                        int bracketOpen = key.indexOf('[');
                        int bracketClose = key.indexOf(']');
                        if (bracketOpen != -1 && bracketClose != -1 && bracketClose > bracketOpen) {
                            String indexStr = key.substring(bracketOpen + 1, bracketClose);
                            try {
                                int index = Integer.parseInt(indexStr);
                                String field = key.substring(bracketClose + 1);
                                if (field.startsWith(".")) field = field.substring(1);

                                Technique technique = techniqueMap.computeIfAbsent(index, k -> new Technique());
                                switch (field) {
                                    case "name": technique.setName(value); break;
                                    case "type": technique.setTypeString(value); break;
                                    case "owner": technique.setOwner(value); break;
                                    case "damage": technique.setDamage(Long.parseLong(value)); break;
                                }
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                    break;
            }
        }

        if (curse != null) builder.setCurse(curse);

        // Преобразуем map в списки, сохраняя порядок по индексам
        List<Sorcerer> sorcerers = new ArrayList<>();
        sorcererMap.keySet().stream().sorted().forEach(idx -> sorcerers.add(sorcererMap.get(idx)));
        builder.setSorcerers(sorcerers);

        List<Technique> techniques = new ArrayList<>();
        techniqueMap.keySet().stream().sorted().forEach(idx -> techniques.add(techniqueMap.get(idx)));
        builder.setTechniques(techniques);

        return builder.build();
    }
}