package com.example.employee;

import com.example.employee.entity.Role;
import com.example.employee.entity.User;
import com.example.employee.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class EmployeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed default users if empty
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User(
                        "admin",
                        "admin@example.com",
                        passwordEncoder.encode("admin123"),
                        Role.ROLE_ADMIN
                );
                userRepository.save(admin);
                System.out.println("✅ Default admin user created (admin / admin123)");
            }
            if (!userRepository.existsByUsername("user")) {
                User user = new User(
                        "user",
                        "user@example.com",
                        passwordEncoder.encode("user123"),
                        Role.ROLE_USER
                );
                userRepository.save(user);
                System.out.println("✅ Default standard user created (user / user123)");
            }
        };
    }
}

