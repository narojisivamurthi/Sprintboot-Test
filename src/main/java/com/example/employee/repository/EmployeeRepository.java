package com.example.employee.repository;

import com.example.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @EntityGraph(attributePaths = {"department", "designation", "city"})
    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"department", "designation", "city"})
    List<Employee> findByDepartmentId(Long departmentId);

    @EntityGraph(attributePaths = {"department", "designation", "city"})
    Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"department", "designation", "city"})
    List<Employee> findAll();

    @Override
    @EntityGraph(attributePaths = {"department", "designation", "city"})
    Page<Employee> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"department", "designation", "city"})
    Optional<Employee> findById(Long id);
}
