package com.tokyomagic.lab3missionarchive.parser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mycompany.lab2missionanalyzerpatterns.model.Mission;
import java.io.File;
import java.io.IOException;

public class XmlMissionParser implements MissionParser {
    private final XmlMapper mapper;

    public XmlMissionParser() {
        mapper = new XmlMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Mission parse(File file) throws IOException {
        return mapper.readValue(file, Mission.class);
    }
}