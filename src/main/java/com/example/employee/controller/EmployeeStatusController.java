package com.example.employee.controller;

import com.example.employee.service.EmployeeStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statuses")
public class EmployeeStatusController {

    private final EmployeeStatusService statusService;

    public EmployeeStatusController(EmployeeStatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllStatuses() {
        return ResponseEntity.ok(statusService.getAllStatuses());
    }
}
