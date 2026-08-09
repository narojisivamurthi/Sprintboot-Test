package com.example.employee.controller;

import com.example.employee.dto.JwtAuthResponse;
import com.example.employee.dto.LoginDto;
import com.example.employee.dto.RegisterDto;
import com.example.employee.dto.UserDto;
import com.example.employee.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/api/auth", "/auth", "/api", ""})
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping({"/login", "/signin"})
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginDto loginDto) {
        JwtAuthResponse response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping({"/register", "/signup"})
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterDto registerDto) {
        String result = authService.register(registerDto);
        return new ResponseEntity<>(Map.of("message", result), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDto userDto = authService.getCurrentUser(authentication.getName());
        return ResponseEntity.ok(userDto);
    }

    @GetMapping({"/users", "/users/all"})
    public ResponseEntity<java.util.List<UserDto>> getAllUsers() {
        java.util.List<UserDto> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
