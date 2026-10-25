package com.example.employee.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "designations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Designation {

    @Id
    private String id;

    @NotBlank(message = "Designation title is required")
    @Size(min = 2, max = 100, message = "Designation title must be between 2 and 100 characters")
    @Indexed(unique = true)
    private String title;

    @NotBlank(message = "Designation code is required")
    @Size(min = 2, max = 20, message = "Designation code must be between 2 and 20 characters")
    @Indexed(unique = true)
    private String code;

    public Designation(String title, String code) {
        this.title = title;
        this.code = code;
    }
}

