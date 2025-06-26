package com.example.demo.entity;

import com.example.demo.entity.enums.ApprovalState;
import com.example.demo.entity.enums.HolidayType;
import com.example.demo.entity.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate startDate;
    private int duration;

    @Enumerated(EnumType.STRING)
    private HolidayType holidayType;

    ApprovalState approvalState;

    @ManyToOne
    private Employee employee;

    public Holiday(LocalDate startDate, int duration, HolidayType holidayType, Employee employee) {
        this.startDate = startDate;
        this.duration = duration;
        this.holidayType = holidayType;
        this.employee = employee;
        this.approvalState = (!holidayType.equals(HolidayType.VACATION) || employee.getRole().equals(Role.ADMIN)) ?
                ApprovalState.APPROVED : ApprovalState.PENDING;
    }

    public void approve() {
        approvalState = ApprovalState.APPROVED;
    }

    public void decline() {
        approvalState = ApprovalState.DECLINED;
    }
}
