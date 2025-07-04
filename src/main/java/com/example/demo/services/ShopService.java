package com.example.demo.services;

import com.example.demo.entity.Complaint;
import com.example.demo.entity.DeliveryItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.Return;
import com.example.demo.entity.enums.ComplaintStatus;
import com.example.demo.entity.enums.Resolution;
import com.example.demo.entity.enums.ReturnStatus;
import com.example.demo.entity.enums.Status;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payload.request.ShopServiceRequest;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ReturnRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShopService {

    private final OrderRepository orderRepository;
    private final ReturnRepository returnRepository;
    private final ComplaintRepository complaintRepository;
    private final DeliveryService deliveryService;

    public ShopService(OrderRepository orderRepository, ReturnRepository returnRepository,
                       ComplaintRepository complaintRepository, DeliveryService deliveryService) {
        this.orderRepository = orderRepository;
        this.returnRepository = returnRepository;
        this.complaintRepository = complaintRepository;
        this.deliveryService = deliveryService;
    }

    public void checkIfOrderExists(Long id) {
        if (!orderRepository.findById(id).isPresent()) {
            throw new NotFoundException("Such order doesn't exist!");
        }
    }

    public void checkIfReturnExists(Long id) {
        if (!returnRepository.findById(id).isPresent()) {
            throw new NotFoundException("Such return doesn't exist!");
        }
    }

    public void checkIfComplaintExists(Long id) {
        if (!complaintRepository.findById(id).isPresent()) {
            throw new NotFoundException("Such complaint doesn't exist!");
        }
    }

    public Order updateOrderStatus(long id, Status status) {
        Order order = orderRepository.findById(id).get();
        order.setStatus(status);
        orderRepository.save(order);
        return order;
    }

    public Return updateReturnStatus(long id, ReturnStatus status) {
        Return r = returnRepository.findById(id).get();
        r.updateStatus(status);
        returnRepository.save(r);
        return r;
    }

    public Complaint updateComplaintStatus(long id, ComplaintStatus status) {
        Complaint complaint = complaintRepository.findById(id).get();
        complaint.updateStatus(status);
        complaintRepository.save(complaint);
        return complaint;
    }

    public Complaint updateComplaintResolution(long id, Resolution resolution) {
        Complaint complaint = complaintRepository.findById(id).get();
        complaint.updateResolution(resolution);
        complaintRepository.save(complaint);
        return complaint;
    }

    public Order addNewOrder(ShopServiceRequest request) {
        List<DeliveryItem> deliveryItems = new ArrayList<>();
        request.getDeliveryItemRequests().forEach(deliveryItemRequest -> {
            deliveryService.addDeliveryItem(deliveryItemRequest, deliveryItems);
        });
        Order order = new Order(request.getFirstName(), request.getLastName(), request.getEmail(),
                request.getPhoneNumber(), request.getStreet(), request.getHouseNumber(),
                request.getCity(), request.getPostalCode(), deliveryItems, request.getScheduledFor());
        orderRepository.save(order);
        return order;
    }

    public Return addNewReturn(ShopServiceRequest request) {
        List<DeliveryItem> deliveryItems = new ArrayList<>();
        request.getDeliveryItemRequests().forEach(deliveryItemRequest -> {
            deliveryService.addDeliveryItem(deliveryItemRequest, deliveryItems);
        });
        Return r = new Return(request.getFirstName(), request.getLastName(), request.getEmail(),
                request.getPhoneNumber(), request.getStreet(), request.getHouseNumber(),
                request.getCity(), request.getPostalCode(), deliveryItems, request.getScheduledFor());
        returnRepository.save(r);
        return r;
    }

    public Complaint addNewComplaint(ShopServiceRequest request) {
        Resolution requestedResolution = request.getRequestedResolution() == null ?
                Resolution.UNRESOLVED : request.getRequestedResolution();
        String fault = request.getFault() == null ?
                "Delivered faulty" : request.getFault();
        List<DeliveryItem> deliveryItems = new ArrayList<>();
        request.getDeliveryItemRequests().forEach(deliveryItemRequest -> {
            deliveryService.addDeliveryItem(deliveryItemRequest, deliveryItems);
        });
        Complaint complaint = new Complaint(requestedResolution, request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPhoneNumber(), request.getStreet(), request.getHouseNumber(),
                request.getCity(), request.getPostalCode(), deliveryItems, request.getScheduledFor(), fault);
        complaintRepository.save(complaint);
        return complaint;
    }

}
