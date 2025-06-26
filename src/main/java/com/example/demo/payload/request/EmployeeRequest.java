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
public class EmployeeRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @Email
    @NotNull
    private String email;
    @NotNull
    private Role role;
    @NotNull
    private ContractRequest contractRequest;
    public Employee extractUser() {
        return new Employee(firstName, lastName, email, role,
                contractRequest.extractContract());
    }
}
