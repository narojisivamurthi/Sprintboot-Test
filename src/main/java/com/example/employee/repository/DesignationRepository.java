package com.example.employee.repository;

import com.example.employee.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {

    Optional<Designation> findByTitle(String title);

    Optional<Designation> findByCode(String code);

    boolean existsByTitle(String title);

    boolean existsByCode(String code);
}
