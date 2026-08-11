package com.example.employee.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    @JsonCreator
    public static Role fromString(String key) {
        if (key == null || key.trim().isEmpty()) {
            return ROLE_USER;
        }
        String upper = key.trim().toUpperCase();
        if (upper.equals("ADMIN") || upper.equals("ROLE_ADMIN")) {
            return ROLE_ADMIN;
        }
        return ROLE_USER;
    }
}

