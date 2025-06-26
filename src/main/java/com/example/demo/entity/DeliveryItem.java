package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class DeliveryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Item item;

    private int quantity;

    public DeliveryItem(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }
}
