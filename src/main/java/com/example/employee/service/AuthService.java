package com.example.employee.service;

import com.example.employee.dto.JwtAuthResponse;
import com.example.employee.dto.LoginDto;
import com.example.employee.dto.RegisterDto;
import com.example.employee.dto.UserDto;

import java.util.List;

public interface AuthService {

    JwtAuthResponse login(LoginDto loginDto);

    String register(RegisterDto registerDto);

    UserDto getCurrentUser(String username);

    List<UserDto> getAllUsers();
}
