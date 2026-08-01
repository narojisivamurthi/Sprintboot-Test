package com.example.employee;

import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EmployeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(EmployeeRepository repository) {
        return args -> {
            repository.save(new Employee("Shiva", "IT", 80000.0));
            repository.save(new Employee("Rahul", "HR", 50000.0));
            repository.save(new Employee("Anjali", "Finance", 70000.0));
            System.out.println("Sample employee data initialized successfully.");
        };
    }
}
