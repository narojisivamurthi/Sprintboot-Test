package com.example.employee.service;

import com.example.employee.dto.JwtAuthResponse;
import com.example.employee.dto.LoginDto;
import com.example.employee.dto.RegisterDto;
import com.example.employee.dto.UserDto;
import com.example.employee.entity.Role;
import com.example.employee.entity.User;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.repository.UserRepository;
import com.example.employee.security.JwtTokenProvider;
import com.example.employee.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private LoginDto loginDto;
    private RegisterDto registerDto;

    @BeforeEach
    void setUp() {
        user = new User("johndoe", "john@example.com", "encoded_password", Role.ROLE_USER);
        user.setId("1");

        loginDto = new LoginDto("johndoe", "password123");
        registerDto = new RegisterDto("johndoe", "john@example.com", "password123", Role.ROLE_USER);
    }


    @Test
    @DisplayName("login should authenticate user and return JwtAuthResponse")
    void login_Success_ShouldReturnJwtAuthResponse() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("mock_jwt_token");
        when(userRepository.findByUsernameOrEmail("johndoe", "johndoe")).thenReturn(Optional.of(user));

        JwtAuthResponse response = authService.login(loginDto);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("mock_jwt_token");
        assertThat(response.getUsername()).isEqualTo("johndoe");
        assertThat(response.getEmail()).isEqualTo("john@example.com");
        assertThat(response.getRole()).isEqualTo(Role.ROLE_USER);
    }

    @Test
    @DisplayName("login should throw ResourceNotFoundException when user record not found")
    void login_UserNotFound_ShouldThrowException() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("mock_jwt_token");
        when(userRepository.findByUsernameOrEmail("johndoe", "johndoe")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.login(loginDto));
    }

    @Test
    @DisplayName("register should encode password and save user successfully")
    void register_Success_ShouldReturnSuccessMessage() {
        when(userRepository.existsByUsername("johndoe")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");

        String result = authService.register(registerDto);

        assertThat(result).isEqualTo("User registered successfully!");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("register should throw IllegalArgumentException when username is taken")
    void register_DuplicateUsername_ShouldThrowException() {
        when(userRepository.existsByUsername("johndoe")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("register should throw IllegalArgumentException when email is registered")
    void register_DuplicateEmail_ShouldThrowException() {
        when(userRepository.existsByUsername("johndoe")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("getCurrentUser should return UserDto when user exists")
    void getCurrentUser_Success_ShouldReturnUserDto() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        UserDto userDto = authService.getCurrentUser("johndoe");

        assertThat(userDto).isNotNull();
        assertThat(userDto.getUsername()).isEqualTo("johndoe");
        assertThat(userDto.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("getAllUsers should return list of UserDtos")
    void getAllUsers_ShouldReturnUserDtoList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> users = authService.getAllUsers();

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUsername()).isEqualTo("johndoe");
    }
}
