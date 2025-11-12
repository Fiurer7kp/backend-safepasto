package com.safepasto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/health")
    public Map<String, String> healthCheck() {
        Map<String, String> status = new HashMap<>();
        
        try {
            jdbcTemplate.execute("SELECT 1");
            status.put("database", "connected");
            status.put("status", "healthy");
            status.put("timestamp", java.time.LocalDateTime.now().toString());
        } catch (Exception e) {
            status.put("database", "disconnected");
            status.put("status", "unhealthy");
            status.put("error", e.getMessage());
        }
        
        return status;
    }
}