package com.example.demo.payload.request;

import com.example.demo.entity.Contract;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequest {
    @NotNull
    private String accountNumber;
    @NotNull
    private int daysOffPerYear;
    @NotNull
    private double salary;
    Contract extractContract() {
        return new Contract(accountNumber, daysOffPerYear, salary);
    }
}
