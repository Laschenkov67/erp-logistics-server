package com.example.demo.entity;

import com.example.demo.entity.enums.ExpenseType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private ExpenseType expenseType;

    private double amount;

    public Expense(ExpenseType expenseType, double amount) {
        this.expenseType = expenseType;
        this.amount = amount;
    }
}
