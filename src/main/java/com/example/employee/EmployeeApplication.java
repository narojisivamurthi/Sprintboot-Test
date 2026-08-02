package com.example.employee;

import com.example.employee.entity.City;
import com.example.employee.entity.Department;
import com.example.employee.entity.Designation;
import com.example.employee.entity.Employee;
import com.example.employee.entity.EmployeeStatus;
import com.example.employee.repository.CityRepository;
import com.example.employee.repository.DepartmentRepository;
import com.example.employee.repository.DesignationRepository;
import com.example.employee.repository.EmployeeRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class EmployeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApplication.class, args);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentJsonInput {
        private String name;
        private String code;
        private String location;

        @JsonCreator
        public static DepartmentJsonInput fromValue(Object value) {
            if (value instanceof Map<?, ?> map) {
                String name = map.containsKey("name") && map.get("name") != null ? map.get("name").toString() : "";
                String code = map.containsKey("code") && map.get("code") != null ? map.get("code").toString() : generateCode(name);
                String location = map.containsKey("location") && map.get("location") != null ? map.get("location").toString() : "Corporate HQ";
                return new DepartmentJsonInput(name, code, location);
            } else if (value instanceof String str) {
                return new DepartmentJsonInput(str, generateCode(str), "Corporate HQ");
            }
            return null;
        }
    }

    @Data
    public static class EmployeeJsonInput {
        private String name;
        private String email;
        private DepartmentJsonInput department;
        private String designation;
        private double salary;
        private LocalDate joiningDate;
        private String city;
        private String status;
    }

    @Bean
    public CommandLineRunner initData(EmployeeRepository employeeRepository,
                                       DepartmentRepository departmentRepository,
                                       DesignationRepository designationRepository,
                                       CityRepository cityRepository) {
        return args -> {
            if (employeeRepository.count() > 0) {
                System.out.println("ℹ️ Database already contains " + employeeRepository.count() + " employee records. Skipping initial seeding.");
                return;
            }

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                TypeReference<List<EmployeeJsonInput>> typeReference = new TypeReference<>() {};

                try (InputStream inputStream = EmployeeApplication.class.getResourceAsStream("/employees.json")) {
                    if (inputStream != null) {
                        List<EmployeeJsonInput> rawEmployees = objectMapper.readValue(inputStream, typeReference);
                        
                        Map<String, Department> departmentMap = new HashMap<>();
                        Map<String, Designation> designationMap = new HashMap<>();
                        Map<String, City> cityMap = new HashMap<>();

                        for (EmployeeJsonInput raw : rawEmployees) {
                            // Seed Departments
                            DepartmentJsonInput deptJson = raw.getDepartment();
                            if (deptJson != null && !departmentMap.containsKey(deptJson.getCode())) {
                                Department dept = departmentRepository.findByCode(deptJson.getCode())
                                        .orElseGet(() -> departmentRepository.save(new Department(deptJson.getName(), deptJson.getCode(), deptJson.getLocation())));
                                departmentMap.put(deptJson.getCode(), dept);
                            }

                            // Seed Designations with unique code handling
                            if (raw.getDesignation() != null && !raw.getDesignation().isBlank() && !designationMap.containsKey(raw.getDesignation().trim())) {
                                String desigTitle = raw.getDesignation().trim();
                                Designation desig = designationRepository.findByTitle(desigTitle)
                                        .orElseGet(() -> {
                                            String code = generateUniqueDesignationCode(desigTitle, designationMap, designationRepository);
                                            return designationRepository.save(new Designation(desigTitle, code));
                                        });
                                designationMap.put(desigTitle, desig);
                            }

                            // Seed Cities
                            if (raw.getCity() != null && !raw.getCity().isBlank() && !cityMap.containsKey(raw.getCity().trim())) {
                                String cityName = raw.getCity().trim();
                                City city = cityRepository.findByName(cityName)
                                        .orElseGet(() -> cityRepository.save(new City(cityName, "N/A", "USA")));
                                cityMap.put(cityName, city);
                            }
                        }

                        List<Employee> employeesToSave = new ArrayList<>();
                        for (EmployeeJsonInput raw : rawEmployees) {
                            Department dept = raw.getDepartment() != null ? departmentMap.get(raw.getDepartment().getCode()) : null;
                            Designation desig = raw.getDesignation() != null ? designationMap.get(raw.getDesignation().trim()) : null;
                            City city = raw.getCity() != null ? cityMap.get(raw.getCity().trim()) : null;
                            
                            EmployeeStatus status = EmployeeStatus.ACTIVE;
                            if (raw.getStatus() != null) {
                                try {
                                    status = EmployeeStatus.valueOf(raw.getStatus().trim().toUpperCase());
                                } catch (IllegalArgumentException ignored) {}
                            }

                            Employee emp = new Employee(
                                    raw.getName(),
                                    raw.getEmail(),
                                    dept,
                                    desig,
                                    raw.getSalary(),
                                    raw.getJoiningDate(),
                                    city,
                                    status
                            );
                            employeesToSave.add(emp);
                        }

                        if (!employeesToSave.isEmpty()) {
                            employeeRepository.saveAll(employeesToSave);
                            System.out.println("✅ Successfully initialized " + departmentMap.size() + " departments, " 
                                    + designationMap.size() + " designations, " 
                                    + cityMap.size() + " cities, and " 
                                    + employeesToSave.size() + " employee records from employees.json into database!");
                        }
                    } else {
                        System.err.println("❌ Could not find employees.json in resources directory!");
                    }
                }
            } catch (IOException e) {
                System.err.println("❌ Failed to load employee data from JSON: " + e.getMessage());
            }
        };
    }

    private static String generateUniqueDesignationCode(String desigTitle, Map<String, Designation> designationMap, DesignationRepository designationRepository) {
        String baseCode = generateCode(desigTitle);
        String code = baseCode;
        int counter = 1;
        while (isCodeTaken(code, designationMap, designationRepository)) {
            code = baseCode + "-" + counter;
            counter++;
        }
        return code;
    }

    private static boolean isCodeTaken(String code, Map<String, Designation> designationMap, DesignationRepository designationRepository) {
        for (Designation d : designationMap.values()) {
            if (d.getCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return designationRepository.existsByCode(code);
    }

    private static String generateCode(String name) {
        if (name == null || name.trim().isEmpty()) return "GEN";
        String[] words = name.split("\\s+");
        if (words.length == 1) {
            return name.substring(0, Math.min(3, name.length())).toUpperCase();
        }
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.equalsIgnoreCase("&")) {
                sb.append(w.charAt(0));
            }
        }
        return sb.toString().toUpperCase();
    }
}
