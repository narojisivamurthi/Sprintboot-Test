package com.example.auth;

import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(scanBasePackages = {"com.example.auth", "com.example.common"})
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDefaultUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            try {
                if (!userRepository.existsByUsername("admin")) {
                    User admin = new User(
                            "admin",
                            "admin@example.com",
                            passwordEncoder.encode("admin123"),
                            Role.ROLE_ADMIN
                    );
                    userRepository.save(admin);
                    System.out.println("✅ [AuthService] Seeded default admin user (admin / admin123)");
                }
                if (!userRepository.existsByUsername("user")) {
                    User user = new User(
                            "user",
                            "user@example.com",
                            passwordEncoder.encode("user123"),
                            Role.ROLE_USER
                    );
                    userRepository.save(user);
                    System.out.println("✅ [AuthService] Seeded default standard user (user / user123)");
                }
            } catch (Exception e) {
                System.err.println("⚠️ [AuthService] Database unavailable during startup seed: " + e.getMessage());
            }
        };
    }
}
