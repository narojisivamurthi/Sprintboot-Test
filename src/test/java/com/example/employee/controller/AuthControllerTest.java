package com.example.employee.controller;

import com.example.employee.dto.JwtAuthResponse;
import com.example.employee.dto.LoginDto;
import com.example.employee.dto.RegisterDto;
import com.example.employee.dto.UserDto;
import com.example.employee.entity.Role;
import com.example.employee.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;
    private LoginDto loginDto;
    private RegisterDto registerDto;
    private JwtAuthResponse jwtAuthResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();

        loginDto = new LoginDto("johndoe", "password123");
        registerDto = new RegisterDto("johndoe", "john@example.com", "password123", Role.ROLE_USER);
        jwtAuthResponse = new JwtAuthResponse("token123", "johndoe", "john@example.com", Role.ROLE_USER);
    }

    @Test
    @DisplayName("POST /api/auth/login should authenticate user and return JWT response")
    void login_ShouldReturnJwtToken() throws Exception {
        when(authService.login(any(LoginDto.class))).thenReturn(jwtAuthResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is("token123")))
                .andExpect(jsonPath("$.username", is("johndoe")));

        verify(authService, times(1)).login(any(LoginDto.class));
    }

    @Test
    @DisplayName("POST /api/auth/register should create user and return HTTP 201 Created")
    void register_ShouldReturnCreatedMessage() throws Exception {
        when(authService.register(any(RegisterDto.class))).thenReturn("User registered successfully!");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("User registered successfully!")));

        verify(authService, times(1)).register(any(RegisterDto.class));
    }

    @Test
    @DisplayName("GET /api/auth/me should return current user details when authenticated")
    void getCurrentUser_ShouldReturnUserDto() throws Exception {
        UserDto userDto = new UserDto(1L, "johndoe", "john@example.com", Role.ROLE_USER, null);
        when(authService.getCurrentUser("johndoe")).thenReturn(userDto);

        org.springframework.security.core.authority.SimpleGrantedAuthority authority =
                new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER");
        Authentication auth = new UsernamePasswordAuthenticationToken("johndoe", "password123", java.util.List.of(authority));

        mockMvc.perform(get("/api/auth/me")
                .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("johndoe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));

        verify(authService, times(1)).getCurrentUser("johndoe");
    }

    @Test
    @DisplayName("GET /api/auth/users should return list of users")
    void getAllUsers_ShouldReturnUserList() throws Exception {
        UserDto userDto = new UserDto(1L, "johndoe", "john@example.com", Role.ROLE_USER, null);
        when(authService.getAllUsers()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/api/auth/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is("johndoe")));

        verify(authService, times(1)).getAllUsers();
    }
}
