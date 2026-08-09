package com.example.employee.dto;

import com.example.employee.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
    private String email;
    private Role role;

    public JwtAuthResponse(String accessToken, String username, String email, Role role) {
        this.accessToken = accessToken;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
