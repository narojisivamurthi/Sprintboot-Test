package com.example.employee.controller;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final MongoTemplate mongoTemplate;

    public HealthController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("backend", "RUNNING");

        Map<String, String> mongoHealth = new HashMap<>();
        try {
            String dbName = mongoTemplate.getDb().getName();
            mongoHealth.put("status", "CONNECTED");
            mongoHealth.put("database", dbName);
        } catch (Exception e) {
            mongoHealth.put("status", "DISCONNECTED");
            mongoHealth.put("error", e.getMessage());
        }
        health.put("mongodb", mongoHealth);

        return ResponseEntity.ok(health);
    }
}
