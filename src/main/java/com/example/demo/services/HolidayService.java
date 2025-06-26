package com.example.demo.services;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Holiday;
import com.example.demo.entity.enums.ApprovalState;
import com.example.demo.entity.enums.HolidayType;
import com.example.demo.exceptions.InvalidRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payload.request.HolidayRequest;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.HolidayRepository;
import com.example.demo.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HolidayService {

    private final EmployeeRepository employeeRepository;
    private final HolidayRepository holidayRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public HolidayService(EmployeeRepository employeeRepository, HolidayRepository holidayRepository,
                          TeamRepository teamRepository) {
        this.employeeRepository = employeeRepository;
        this.holidayRepository = holidayRepository;
        this.teamRepository = teamRepository;
    }

    public void checkIfCanTakeDaysOff(long id, HolidayRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        if (holidayRepository.findByEmployeeId(id).isPresent()) {
            List<Holiday> holidays = holidayRepository.findByEmployeeId(id).get();
            int daysOffTaken = holidays.stream()
                    .filter(holiday -> holiday.getHolidayType().equals(HolidayType.VACATION))
                    .filter(holiday -> holiday.getStartDate().getYear() == LocalDate.now().getYear())
                    .map(Holiday::getDuration)
                    .mapToInt(Integer::intValue)
                    .sum();
            int daysOffPerYear = employee.getContract().getDaysOffPerYear();
            int requestedDuration = 0;
            for (int i = 0; i < request.getDuration(); i++) {
                if (!request.getStartDate().plusDays(i).getDayOfWeek().equals(DayOfWeek.SATURDAY) &&
                        !request.getStartDate().plusDays(i).getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                    requestedDuration++;
                }
            }
            if (daysOffTaken == daysOffPerYear) {
                throw new InvalidRequestException("You've already used all your vacation days for this year!");
            } else if ((daysOffPerYear - daysOffTaken) < requestedDuration) {
                throw new InvalidRequestException("You don't have enough available days to take such a long vacation this year!");
            }
        }
    }

    public List<Holiday> getHolidayRequests(Long managerId) {
        List<Holiday> holidays = new ArrayList<>();
        if (!teamRepository.findByManagerId(managerId).isPresent()) {
            return new ArrayList<>();
        }
        List<Employee> employees = teamRepository.findByManagerId(managerId)
                .get().getEmployees();
        if (employees.size() == 0) {
            return new ArrayList<>();
        }
        employees.forEach(employee -> {
            if (holidayRepository
                    .findByEmployeeId(employee.getId()).isPresent()) {
                holidays.addAll(holidayRepository
                        .findByEmployeeId(employee.getId()).get());
            }
        });
        return holidays.size() > 0 ? holidays : new ArrayList<>();
    }

    public void checkIfHolidayPending(long id) {
        if (!holidayRepository.findById(id)
                .orElseThrow(NotFoundException::new)
                .getApprovalState().equals(ApprovalState.PENDING)) {
            throw new InvalidRequestException("This holiday has already been managed!");
        }
    }

    public Holiday addHoliday(HolidayRequest request, Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        Holiday holiday = new Holiday(request.getStartDate(), request.getDuration(),
                request.getHolidayType(), employee);
        holidayRepository.save(holiday);
        return holiday;
    }
}
