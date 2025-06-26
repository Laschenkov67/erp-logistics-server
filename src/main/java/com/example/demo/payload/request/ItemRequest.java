package com.example.demo.payload.request;

import com.example.demo.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @NotNull
    private String name;
    @NotNull
    private double stockPrice;
    @NotNull
    private double price;
    public Item extractItem() {
        return new Item(name, 0, stockPrice, price);
    }
}
