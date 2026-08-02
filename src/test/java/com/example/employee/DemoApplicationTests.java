package com.example.employee;

import com.example.employee.dto.EmployeeRequestDTO;
import com.example.employee.entity.EmployeeStatus;
import com.example.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private EmployeeService employeeService;

    @Test
    void contextLoads() {
    }

    @Test
    void testEmployeeRequestDTOWithNestedDepartment() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String jsonPayload = """
        {
            "name": "Shiva Naroji",
            "email": "shiva.naroji@company.com",
            "department": {
                "id": 1,
                "name": "Engineering",
                "code": "ENG",
                "location": "Corporate HQ"
            },
            "designation": "Tech Lead",
            "salary": 145000.0,
            "joiningDate": "2020-03-15",
            "city": "Bangalore",
            "status": "ACTIVE"
        }
        """;

        EmployeeRequestDTO dto = mapper.readValue(jsonPayload, EmployeeRequestDTO.class);
        assertEquals(1L, dto.getDepartmentId());
        assertEquals("Engineering", dto.getDepartmentName());
        assertEquals("ENG", dto.getDepartmentCode());
        assertEquals("Tech Lead", dto.getDesignationTitle());
        assertEquals("Shiva Naroji", dto.getName());
        assertEquals(EmployeeStatus.ACTIVE, dto.getStatus());
    }

    @Test
    void testSalaryValidationFailureForLowSalaryWithHighExperience() {
        EmployeeRequestDTO lowSalaryDto = EmployeeRequestDTO.builder()
                .name("Low Pay Test")
                .email("lowpay.test@company.com")
                .departmentName("Engineering")
                .designationTitle("Senior Software Lead")
                .salary(10000.0) // Low salary ($10k)
                .joiningDate(LocalDate.of(2018, 1, 1)) // ~8 years exp -> minimum required is 8 * 18,000 = $144,000
                .cityName("Bangalore")
                .status(EmployeeStatus.ACTIVE)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.createEmployee(lowSalaryDto);
        });

        assertTrue(exception.getMessage().contains("Minimum required salary is"));
    }
}
