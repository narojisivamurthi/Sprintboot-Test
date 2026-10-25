package com.example.auth.service;

import com.example.auth.dto.JwtAuthResponse;
import com.example.auth.dto.LoginDto;
import com.example.auth.dto.RegisterDto;
import com.example.auth.dto.UserDto;

import java.util.List;

public interface AuthService {

    JwtAuthResponse login(LoginDto loginDto);

    String register(RegisterDto registerDto);

    UserDto getCurrentUser(String username);

    List<UserDto> getAllUsers();
}
