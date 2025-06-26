package com.example.demo.dto;

import com.example.demo.entity.Contract;
import com.example.demo.entity.Employee;
import com.example.demo.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Contract contract;
    boolean isPasswordValid;

    public UserDTO(Employee employee) {
        this.id = employee.getId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
        this.role = employee.getRole();
        this.contract = employee.getContract();
        this.isPasswordValid = employee.isPasswordValid();
    }
}
