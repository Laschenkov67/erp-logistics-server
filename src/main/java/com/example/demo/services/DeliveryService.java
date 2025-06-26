package com.example.demo.services;


import com.example.demo.entity.Delivery;
import com.example.demo.entity.DeliveryItem;
import com.example.demo.entity.Item;
import com.example.demo.entity.Order;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payload.request.DeliveryItemRequest;
import com.example.demo.payload.request.DeliveryRequest;
import com.example.demo.repository.DeliveryItemRepository;
import com.example.demo.repository.DeliveryRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class DeliveryService {

    private final ItemRepository itemRepository;
    private final DeliveryItemRepository deliveryItemRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public DeliveryService(ItemRepository itemRepository, DeliveryItemRepository deliveryItemRepository,
                           DeliveryRepository deliveryRepository, OrderRepository orderRepository) {
        this.itemRepository = itemRepository;
        this.deliveryItemRepository = deliveryItemRepository;
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
    }

    public Delivery addDelivery(DeliveryRequest request) {
        List<DeliveryItem> deliveryItems = new ArrayList<>();
        request.getDeliveryItemRequests().forEach(deliveryItemRequest -> {
            addDeliveryItem(deliveryItemRequest, deliveryItems);
        });
        Delivery delivery = new Delivery(deliveryItems, request.getScheduledFor());
        deliveryRepository.save(delivery);
        return delivery;
    }

    public void addDeliveryItem(DeliveryItemRequest deliveryItemRequest, List<DeliveryItem> deliveryItems) {
        if (itemRepository.findById(deliveryItemRequest.getItemId()).isPresent()) {
            Item item = itemRepository.findById(deliveryItemRequest.getItemId()).get();
            DeliveryItem deliveryItem = new DeliveryItem(item, deliveryItemRequest.getQuantity());
            deliveryItemRepository.save(deliveryItem);
            deliveryItems.add(deliveryItem);
        }
    }


    public void checkIfDeliveryExists(long id) {
        if (!deliveryRepository.findById(id).isPresent()) {
            throw new NotFoundException("Such delivery doesn't exist!");
        }
    }

    public List<DeliveryItemRequest> getRecommendedDelivery() {
        List<DeliveryItemRequest> deliveryItems = new ArrayList<>();
        List<Item> items = itemRepository.findAll();
        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> DAYS.between(order.getSubmissionDate(), LocalDate.now()) < 30)
                .collect(Collectors.toList());
        if (orders == null || orders.size() == 0) {
            return deliveryItems;
        }
        for (Item item : items) {
            int sum = 0;
            for (Order order : orders) {
                for (DeliveryItem deliveryItem: order.getDeliveryItems()) {
                    if (deliveryItem.getItem().getId() == item.getId()) {
                        sum += deliveryItem.getQuantity();
                    }
                }
            }
            if (sum > 0) {
                deliveryItems.add(new DeliveryItemRequest(item.getId(), sum));
            }
        }
        return deliveryItems;
    }
}
