package com.example.demo.controllers;

import com.example.demo.entity.SpecialPlan;
import com.example.demo.entity.DailyPlan;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payload.request.DailyPlanRequest;
import com.example.demo.payload.request.SpecialPlanRequest;
import com.example.demo.repository.DailyPlanRepository;
import com.example.demo.repository.SpecialPlanRepository;
import com.example.demo.services.PlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class PlanningController {

    private final DailyPlanRepository dailyPlanRepository;
    private final SpecialPlanRepository specialPlanRepository;
    private final PlanningService planningService;

    @Autowired
    public PlanningController(DailyPlanRepository dailyPlanRepository, SpecialPlanRepository specialPlanRepository,
                              PlanningService planningService) {
        this.dailyPlanRepository = dailyPlanRepository;
        this.specialPlanRepository = specialPlanRepository;
        this.planningService = planningService;
    }

    @GetMapping("/daily-plan")
    @ResponseStatus(HttpStatus.OK)
    public DailyPlan getDailyPlan() {
        return dailyPlanRepository.findById(1L)
                .orElseThrow(NotFoundException::new);
    }

    @PutMapping("/daily-plan")
    @ResponseStatus(HttpStatus.OK)
    public DailyPlan updateDailyPlan(@RequestBody DailyPlanRequest request) {
        return planningService.updateDailyPlan(request);
    }

    @GetMapping("/scheduled-orders")
    @ResponseStatus(HttpStatus.OK)
    public int getOrdersScheduledForDay(@RequestParam("when") String when) {
        return planningService.getOrdersForDay(when).size();
    }

    @GetMapping("/scheduled-returns")
    @ResponseStatus(HttpStatus.OK)
    public int getReturnsScheduledForDay(@RequestParam("when") String when) {
        return planningService.getReturnsForDay(when).size();
    }

    @GetMapping("/scheduled-complaints")
    @ResponseStatus(HttpStatus.OK)
    public int getComplaintsScheduledForDay(@RequestParam("when") String when) {
        return planningService.getComplaintsForDay(when).size();
    }

    @GetMapping("/special-plans")
    @ResponseStatus(HttpStatus.OK)
    public List<SpecialPlan> getSpecialPlans() {
        return specialPlanRepository.findAll();
    }

    @GetMapping("/special-plan")
    @ResponseStatus(HttpStatus.OK)
    public SpecialPlan findSpecialPlan(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                       @RequestParam("day") LocalDate day) {
        Optional<List<SpecialPlan>> byDay = specialPlanRepository.findByDay(day);
        return byDay.isPresent() ? byDay.get().get(byDay.get().size()-1) : new SpecialPlan();
    }

    @PostMapping("/special-plan")
    @ResponseStatus(HttpStatus.CREATED)
    public SpecialPlan addSpecialPlan(@RequestBody SpecialPlanRequest request) {
        SpecialPlan specialPlan = request.extractSpecialPlan();
        specialPlanRepository.save(specialPlan);
        return specialPlan;
    }
}
