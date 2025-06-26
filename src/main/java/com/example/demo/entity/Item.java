package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private int quantity;
    private double stockPrice;
    private double originalPrice;

    @Setter
    private double currentPrice;

    public Item(String name, int quantity, double stockPrice, double price) {
        this.name = name;
        this.quantity = quantity;
        this.stockPrice = stockPrice;
        this.originalPrice = price;
        this.currentPrice = price;
    }

    public void supply(int q) {
        quantity += q;
    }

    public void sell(int q) {
        quantity -= q;
    }
}
