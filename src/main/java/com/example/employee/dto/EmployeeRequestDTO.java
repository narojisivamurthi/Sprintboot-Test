package com.example.employee.dto;

import com.example.employee.entity.EmployeeStatus;
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

    // Provide designationId OR designationTitle OR designationCode OR nested "designation" object
    private Long designationId;
    private String designationTitle;
    private String designationCode;

    @Positive(message = "Salary must be greater than zero")
    private double salary;

    private LocalDate joiningDate;

    // Provide cityId OR cityName OR nested "city" object
    private Long cityId;
    private String cityName;

    private EmployeeStatus status; // ACTIVE, INACTIVE, ON_LEAVE

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

    @JsonProperty("designation")
    public void setDesignation(Object designation) {
        if (designation instanceof Map<?, ?> map) {
            if (map.containsKey("id") && map.get("id") != null) {
                try {
                    this.designationId = Long.valueOf(map.get("id").toString());
                } catch (NumberFormatException ignored) {}
            }
            if (map.containsKey("code") && map.get("code") != null) {
                this.designationCode = map.get("code").toString();
            }
            if (map.containsKey("title") && map.get("title") != null) {
                this.designationTitle = map.get("title").toString();
            }
        } else if (designation instanceof String str) {
            this.designationTitle = str;
        } else if (designation instanceof Number num) {
            this.designationId = num.longValue();
        }
    }

    @JsonProperty("city")
    public void setCity(Object city) {
        if (city instanceof Map<?, ?> map) {
            if (map.containsKey("id") && map.get("id") != null) {
                try {
                    this.cityId = Long.valueOf(map.get("id").toString());
                } catch (NumberFormatException ignored) {}
            }
            if (map.containsKey("name") && map.get("name") != null) {
                this.cityName = map.get("name").toString();
            }
        } else if (city instanceof String str) {
            this.cityName = str;
        } else if (city instanceof Number num) {
            this.cityId = num.longValue();
        }
    }
}
