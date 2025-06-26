package com.example.demo.payload.request;

import com.example.demo.entity.SpecialPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialPlanRequest {

    @NotNull
    private String description;

    @NotNull
    private LocalDate day;

    @NotNull
    private int employeesPerDay;

    @NotNull
    private int ordersPerDay;

    @NotNull
    private int returnsPerDay;

    @NotNull
    private int complaintsResolvedPerDay;

    public SpecialPlan extractSpecialPlan() {
        return new SpecialPlan(description, day, employeesPerDay, ordersPerDay, returnsPerDay,
                complaintsResolvedPerDay);
    }
}
