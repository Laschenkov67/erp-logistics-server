package com.example.demo.entity;

import com.example.demo.entity.EstimatedCosts;
import com.example.demo.entity.Expense;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class CurrentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany
    private List<Expense> expenses;

    @ElementCollection
    private List<Double> income;

    @OneToOne
    private EstimatedCosts estimatedCosts;

    @Setter
    private LocalDate startDate;

    public CurrentReport(EstimatedCosts estimatedCosts) {
        this.estimatedCosts = estimatedCosts;
        expenses = new ArrayList<>();
        income = new ArrayList<>();
        startDate = LocalDate.now();
    }

    public void clearReport() {
        income = new ArrayList<>();
        expenses = new ArrayList<>();
        startDate = LocalDate.now();
    }

    public void updateEstimatedCosts(EstimatedCosts reestimatedCosts) {
        this.estimatedCosts = reestimatedCosts;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public void addIncome(double amount) {
        income.add(amount);
    }
}
