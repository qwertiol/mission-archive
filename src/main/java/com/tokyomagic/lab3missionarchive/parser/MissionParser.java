package com.tokyomagic.lab3missionarchive.parser;

import com.mycompany.lab2missionanalyzerpatterns.model.Mission;
import java.io.File;
import java.io.IOException;

public interface MissionParser {
    Mission parse(File file) throws IOException;
}
