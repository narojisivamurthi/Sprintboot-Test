package com.example.employee.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    private String id;

    @NotBlank(message = "City name is required")
    @Size(min = 2, max = 50, message = "City name must be between 2 and 50 characters")
    @Indexed(unique = true)
    private String name;

    private String state;

    private String country;

    public City(String name, String state, String country) {
        this.name = name;
        this.state = state;
        this.country = country;
    }
}

