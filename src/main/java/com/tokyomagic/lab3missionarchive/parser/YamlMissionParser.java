package com.tokyomagic.lab3missionarchive.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mycompany.lab2missionanalyzerpatterns.model.Mission;
import java.io.File;
import java.io.IOException;

public class YamlMissionParser implements MissionParser {
    private final ObjectMapper mapper;

    public YamlMissionParser() {
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Mission parse(File file) throws IOException {
        return mapper.readValue(file, Mission.class);
    }
}