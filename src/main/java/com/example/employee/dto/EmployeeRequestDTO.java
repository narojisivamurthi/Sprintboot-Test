package com.example.employee.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestDTO {

    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 50, message = "Employee name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    // Provide departmentId OR departmentCode OR departmentName OR nested "department" object
    private Long departmentId;
    private String departmentCode;
    private String departmentName;

    @NotBlank(message = "Designation is required")
    private String designation;

    @Positive(message = "Salary must be greater than zero")
    private double salary;

    private LocalDate joiningDate;

    private String city;

    private String status; // ACTIVE, INACTIVE, ON_LEAVE

    @JsonProperty("department")
    public void setDepartment(Object department) {
        if (department instanceof Map<?, ?> map) {
            if (map.containsKey("id") && map.get("id") != null) {
                try {
                    this.departmentId = Long.valueOf(map.get("id").toString());
                } catch (NumberFormatException ignored) {}
            }
            if (map.containsKey("code") && map.get("code") != null) {
                this.departmentCode = map.get("code").toString();
            }
            if (map.containsKey("name") && map.get("name") != null) {
                this.departmentName = map.get("name").toString();
            }
        } else if (department instanceof String str) {
            this.departmentName = str;
        } else if (department instanceof Number num) {
            this.departmentId = num.longValue();
        }
    }
}
