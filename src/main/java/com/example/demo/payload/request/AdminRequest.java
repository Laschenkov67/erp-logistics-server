package com.example.demo.payload.request;

import com.example.demo.entity.Employee;
import com.example.demo.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @Email
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private ContractRequest contractRequest;
    public Employee extractUser() {
        Employee employee = new Employee(firstName, lastName, email, Role.ADMIN,
                contractRequest.extractContract());
        employee.changePassword(password);
        return employee;
    }
}
