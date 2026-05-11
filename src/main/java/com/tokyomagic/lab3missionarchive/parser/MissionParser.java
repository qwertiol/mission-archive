package com.tokyomagic.lab3missionarchive.parser;

import com.tokyomagic.lab3missionarchive.model.Mission;
import java.io.File;
import java.io.IOException;

public interface MissionParser {
    Mission parse(File file) throws IOException;
}
