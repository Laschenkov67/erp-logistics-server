package com.example.demo.payload.request;

import com.example.demo.entity.enums.HolidayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayRequest {
    @NotNull
    private LocalDate startDate;
    @NotNull
    private int duration;
    @NotNull
    private HolidayType holidayType;
}
