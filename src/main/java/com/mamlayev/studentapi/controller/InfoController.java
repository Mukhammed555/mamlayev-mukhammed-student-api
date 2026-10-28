package com.mamlayev.studentapi.controller;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shows who owns this application and which version is running.
 */
@RestController
public class InfoController {

    private final String studentName;
    private final String applicationName;
    private final String version;

    public InfoController(@Value("${app.student.name:Mukhammed Mamlayev}") String studentName,
                          @Value("${spring.application.name:mamlayev-mukhammed-student-api}") String applicationName,
                          ObjectProvider<BuildProperties> buildProperties) {
        this.studentName = studentName;
        this.applicationName = applicationName;
        BuildProperties build = buildProperties.getIfAvailable();
        this.version = build != null ? build.getVersion() : "dev";
    }

    @GetMapping({"/", "/api/info"})
    public Map<String, Object> info() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("student", studentName);
        info.put("application", applicationName);
        info.put("version", version);
        info.put("message", "Student API of " + studentName + " is running");
        info.put("timestamp", Instant.now().toString());
        return info;
    }
}
