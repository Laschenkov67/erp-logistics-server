package com.example.demo.services;

import com.example.demo.entity.Complaint;
import com.example.demo.entity.DailyPlan;
import com.example.demo.entity.Order;
import com.example.demo.entity.Return;
import com.example.demo.exceptions.InvalidRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payload.request.DailyPlanRequest;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.DailyPlanRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ReturnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanningService {

    private final DailyPlanRepository dailyPlanRepository;
    private final OrderRepository orderRepository;
    private final ReturnRepository returnRepository;
    private final ComplaintRepository complaintRepository;

    @Autowired
    public PlanningService(DailyPlanRepository dailyPlanRepository, OrderRepository orderRepository,
                           ReturnRepository returnRepository, ComplaintRepository complaintRepository) {
        this.dailyPlanRepository = dailyPlanRepository;
        this.orderRepository = orderRepository;
        this.returnRepository = returnRepository;
        this.complaintRepository = complaintRepository;
    }

    public DailyPlan updateDailyPlan(DailyPlanRequest request) {
        DailyPlan dailyPlan = dailyPlanRepository.findById(1L)
                .orElseThrow(NotFoundException::new);
        dailyPlan.setEmployeesPerDay(request.getEmployeesPerDay());
        dailyPlan.setOrdersPerDay(request.getOrdersPerDay());
        dailyPlan.setReturnsPerDay(request.getReturnsPerDay());
        dailyPlan.setComplaintsResolvedPerDay(request.getComplaintsResolvedPerDay());
        dailyPlanRepository.save(dailyPlan);
        return dailyPlan;
    }

    public List<Order> getOrdersForDay(String when) {
        List<Order> orders = orderRepository.findAll();
        LocalDate today = LocalDate.now();
        switch (when) {
            case "today":
                orders = orders.stream()
                        .filter(order -> order.getScheduledFor().equals(today))
                        .collect(Collectors.toList());
                break;
            case "tomorrow":
                orders = orders.stream()
                        .filter(order -> order.getScheduledFor().equals(today.plusDays(1)))
                        .collect(Collectors.toList());
                break;
            case "in2days":
                orders = orders.stream()
                        .filter(order -> order.getScheduledFor().equals(today.plusDays(2)))
                        .collect(Collectors.toList());
                break;
            default:
                throw new InvalidRequestException("Invalid query parameter!");
        }
        return orders;
    }

    public List<Return> getReturnsForDay(String when) {
        List<Return> returns = returnRepository.findAll();
        LocalDate today = LocalDate.now();
        switch (when) {
            case "today":
                returns = returns.stream()
                        .filter(order -> order.getScheduledFor().equals(today))
                        .collect(Collectors.toList());
                break;
            case "tomorrow":
                returns = returns.stream()
                        .filter(order -> order.getScheduledFor().equals(today.plusDays(1)))
                        .collect(Collectors.toList());
                break;
            case "in2days":
                returns = returns.stream()
                        .filter(order -> order.getScheduledFor().equals(today.plusDays(2)))
                        .collect(Collectors.toList());
                break;
            default:
                throw new InvalidRequestException("Invalid query parameter!");
        }
        return returns;
    }

    public List<Complaint> getComplaintsForDay(String when) {
        List<Complaint> complaints = complaintRepository.findAll();
        LocalDate today = LocalDate.now();
        switch (when) {
            case "today":
                complaints = complaints.stream()
                        .filter(order -> order.getScheduledFor().equals(today))
                        .collect(Collectors.toList());
                break;
            case "tomorrow":
                complaints = complaints.stream()
                        .filter(order -> order.getScheduledFor().equals(today.plusDays(1)))
                        .collect(Collectors.toList());
                break;
            case "in2days":
                complaints = complaints.stream()
                        .filter(order -> order.getScheduledFor().equals(today.plusDays(2)))
                        .collect(Collectors.toList());
                break;
            default:
                throw new InvalidRequestException("Invalid query parameter!");
        }
        return complaints;
    }
}
