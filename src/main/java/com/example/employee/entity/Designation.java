package com.example.employee.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "designations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Designation title is required")
    @Size(min = 2, max = 100, message = "Designation title must be between 2 and 100 characters")
    @Column(nullable = false, unique = true)
    private String title;

    @NotBlank(message = "Designation code is required")
    @Size(min = 2, max = 20, message = "Designation code must be between 2 and 20 characters")
    @Column(nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "designation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Employee> employees = new ArrayList<>();

    public Designation(String title, String code) {
        this.title = title;
        this.code = code;
    }
}
