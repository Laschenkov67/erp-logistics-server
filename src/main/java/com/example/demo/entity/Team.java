package com.example.demo.entity;

import com.example.demo.entity.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private Employee manager;

    @ManyToMany
    private List<Employee> employees;

    public Team(Role role) {
        this.role = role;
        employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        if(employee.getRole().name().contains("ADMIN")) {
            if(employee.getRole().equals(Role.ADMIN) && manager == null && role.equals(Role.ADMIN)) {
                manager = employee;
            } else if (!employee.getRole().equals(Role.ADMIN) && role.equals(Role.ADMIN)) {
                employees.add(employee);
            } else if (!employee.getRole().equals(Role.ADMIN) && manager == null) {
                manager = employee;
            }
        } else {
            if (employee.getRole().name().contains(role.name())) {
                employees.add(employee);
            }
        }
    }

    public void removeManager() {
        manager = null;
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }
}
