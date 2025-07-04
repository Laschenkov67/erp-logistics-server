package com.example.demo.repository;

import com.example.demo.entity.Employee;
import com.example.demo.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<List<Employee>> findByRole(Role role);
    Optional<Employee> findByEmail(String email);
}
