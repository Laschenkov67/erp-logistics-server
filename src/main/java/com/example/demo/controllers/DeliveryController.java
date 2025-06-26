package com.example.demo.controllers;

import com.example.demo.entity.Delivery;
import com.example.demo.payload.request.DeliveryItemRequest;
import com.example.demo.payload.request.DeliveryRequest;
import com.example.demo.repository.DeliveryRepository;
import com.example.demo.services.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryRepository deliveryRepository, DeliveryService deliveryService) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryService = deliveryService;
    }

    @GetMapping("/deliveries")
    @ResponseStatus(HttpStatus.OK)
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    @PostMapping("/deliveries")
    @ResponseStatus(HttpStatus.CREATED)
    public Delivery addNewDelivery(@RequestBody DeliveryRequest request) {
        return deliveryService.addDelivery(request);
    }

    @GetMapping("/deliveries/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Delivery getOneDelivery(@PathVariable("id") long id) {
        deliveryService.checkIfDeliveryExists(id);
        return deliveryRepository.findById(id).get();
    }

    @PostMapping("/deliveries/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Delivery confirmDelivery(@PathVariable("id") long id) {
        deliveryService.checkIfDeliveryExists(id);
        Delivery delivery = deliveryRepository.findById(id).get();
        delivery.confirm();
        deliveryRepository.save(delivery);
        return delivery;
    }

    @GetMapping("/deliveries/recommended-delivery")
    public List<DeliveryItemRequest> recommendDelivery() {
        return deliveryService.getRecommendedDelivery();
    }
}
