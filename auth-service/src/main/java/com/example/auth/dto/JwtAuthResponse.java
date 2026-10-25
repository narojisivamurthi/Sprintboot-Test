package com.example.auth.dto;

import com.example.auth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accessToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private String username;
    private String email;
    private Role role;

    public JwtAuthResponse(String accessToken, String username, String email, Role role) {
        this.accessToken = accessToken;
        this.username = username;
        this.email = email;
        this.role = role;
        this.tokenType = "Bearer";
    }
}
