package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class SpecialPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;
    private LocalDate day;
    private int employeesPerDay;
    private int ordersPerDay;
    private int returnsPerDay;
    private int complaintsResolvedPerDay;

    public SpecialPlan(String description, LocalDate day, int employeesPerDay, int ordersPerDay,
                       int returnsPerDay, int complaintsResolvedPerDay) {
        this.description = description;
        this.day = day;
        this.employeesPerDay = employeesPerDay;
        this.ordersPerDay = ordersPerDay;
        this.returnsPerDay = returnsPerDay;
        this.complaintsResolvedPerDay = complaintsResolvedPerDay;
    }
}
