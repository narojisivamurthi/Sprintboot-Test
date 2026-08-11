package com.example.employee.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;

@Document(collection = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    private String id;

    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 50, message = "Employee name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Indexed(unique = true)
    private String email;

    @DocumentReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Department department;

    @DocumentReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Designation designation;

    @Positive(message = "Salary must be greater than zero")
    private double salary;

    private LocalDate joiningDate;

    @DocumentReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private City city;

    private EmployeeStatus status;

    public Employee(String name, String email, Department department, Designation designation, double salary, LocalDate joiningDate, City city, EmployeeStatus status) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.designation = designation;
        this.salary = salary;
        this.joiningDate = joiningDate;
        this.city = city;
        this.status = status;
    }
}

