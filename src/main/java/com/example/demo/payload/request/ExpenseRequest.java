package com.example.demo.payload.request;

import com.example.demo.entity.Expense;
import com.example.demo.entity.enums.ExpenseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequest {
    @NotNull
    private ExpenseType expenseType;
    @NotNull
    private double amount;
    public Expense extractExpense() {
        return new Expense(expenseType, amount);
    }
}
