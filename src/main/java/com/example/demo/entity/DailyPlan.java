package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Getter
@Setter
public class DailyPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int employeesPerDay;
    private int ordersPerDay;
    private int returnsPerDay;
    private int complaintsResolvedPerDay;

    public DailyPlan() {
        this.employeesPerDay = 5;
        this.ordersPerDay = 15;
        this.returnsPerDay = 5;
        this.complaintsResolvedPerDay = 5;
    }
}
