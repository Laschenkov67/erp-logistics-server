package com.example.demo.services;

import com.example.demo.entity.Item;
import com.example.demo.exceptions.InvalidRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> updateItems(double multiplier, String query) {
        List<Item> items = itemRepository.findAll();
        if (query != null && !query.equals("")) {
            items = items.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }
        List<Item> updatedItems = new ArrayList<>();
        items.forEach(item -> {
            item.setCurrentPrice(item.getOriginalPrice() * multiplier);
            itemRepository.save(item);
            updatedItems.add(item);
        });
        return updatedItems;
    }

    public void checkIfItemExists(long id) {
        if (!itemRepository.findById(id).isPresent()) {
            throw new NotFoundException("Such item doesn't exist!");
        }
    }

    public void checkIfIsEnough(Item item, int q) {
        if (q > item.getQuantity()) {
            throw new InvalidRequestException("There's not enough of this item!");
        }
    }

    public Item supplyItem(long id, int quantity) {
        Item item = itemRepository.findById(id).get();
        item.supply(quantity);
        itemRepository.save(item);
        return item;
    }

    public Item buyItem(long id, int quantity) {
        Item item = itemRepository.findById(id).get();
        checkIfIsEnough(item, quantity);
        item.sell(quantity);
        itemRepository.save(item);
        return item;
    }
}
