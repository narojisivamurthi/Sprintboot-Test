package com.example.employee.service;

import com.example.employee.entity.EmployeeStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeStatusService {

    public List<String> getAllStatuses() {
        return Arrays.stream(EmployeeStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
