package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String accountNumber;
    private int daysOffPerYear;
    private double salary;

    public Contract(String accountNumber, int daysOffPerYear, double salary) {
        this.accountNumber = accountNumber;
        this.daysOffPerYear = daysOffPerYear;
        this.salary = salary;
    }
}
