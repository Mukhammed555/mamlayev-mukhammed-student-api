package com.mamlayev.studentapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Prints the student's name to the application log on startup,
 * so it is visible in "docker logs" and in the Jenkins console.
 */
@Component
public class StartupLogger {

    private static final Logger log = LoggerFactory.getLogger(StartupLogger.class);

    private final String studentName;

    public StartupLogger(@Value("${app.student.name:Mukhammed Mamlayev}") String studentName) {
        this.studentName = studentName;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        log.info("=== Student API of {} is up and running ===", studentName);
    }
}
