package com.tokyomagic.lab3missionarchive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartMessage implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartMessage.class);

    @Override
    public void run(String... args) {
        log.info("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                 "  Tokyo Magic College Mission Archive started!\n" +
                 "  API documentation : http://localhost:8080/swagger-ui.html\n" +
                 "  Web interface     : http://localhost:8080\n" +
                 "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}