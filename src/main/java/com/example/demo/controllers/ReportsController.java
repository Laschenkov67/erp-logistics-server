package com.example.demo.controllers;

import com.example.demo.entity.CurrentReport;
import com.example.demo.entity.MonthlyReport;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payload.request.EstimatedCostsRequest;
import com.example.demo.payload.request.ExpenseRequest;
import com.example.demo.repository.CurrentReportRepository;
import com.example.demo.repository.MonthlyReportRepository;
import com.example.demo.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ReportsController {

    private final String REPORT_NOT_FOUND = "Such report doesn't exist!";

    private final CurrentReportRepository currentReportRepository;
    private final MonthlyReportRepository monthlyReportRepository;
    private final ReportService reportService;

    @Autowired
    public ReportsController(CurrentReportRepository currentReportRepository,
                             MonthlyReportRepository monthlyReportRepository,
                             ReportService reportService) {
        this.currentReportRepository = currentReportRepository;
        this.monthlyReportRepository = monthlyReportRepository;
        this.reportService = reportService;
    }

    @GetMapping("/reports")
    @ResponseStatus(HttpStatus.OK)
    public List<MonthlyReport> getReports() {
        if (reportService.shouldSaveReport()) {
            reportService.saveReport();
        }
        return monthlyReportRepository.findAll();
    }

    @GetMapping("/reports/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MonthlyReport getReport(@PathVariable("id") long id) {
        if (reportService.shouldSaveReport()) {
            reportService.saveReport();
        }
        return monthlyReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(REPORT_NOT_FOUND));
    }

    @GetMapping("/current-report")
    @ResponseStatus(HttpStatus.OK)
    public CurrentReport getCurrentReport() {
        if (reportService.shouldSaveReport()) {
            reportService.saveReport();
        }
        return currentReportRepository.findById((long) 1)
                .orElseThrow(() -> new NotFoundException(REPORT_NOT_FOUND));
    }

    @PutMapping("/current-report")
    @ResponseStatus(HttpStatus.OK)
    public CurrentReport recalculateCosts(@RequestBody EstimatedCostsRequest reestimatedCosts) {
        if (reportService.shouldSaveReport()) {
            reportService.saveReport();
        }
        return reportService.recalculateCosts(reestimatedCosts);
    }

    @PostMapping("/current-report/income")
    @ResponseStatus(HttpStatus.OK)
    public CurrentReport addIncome(@RequestBody double amount) {
        if (reportService.shouldSaveReport()) {
            reportService.saveReport();
        }
        return reportService.addIncome(amount);
    }

    @PostMapping("/current-report/expense")
    @ResponseStatus(HttpStatus.OK)
    public CurrentReport addExpense(@RequestBody ExpenseRequest request) {
        if (reportService.shouldSaveReport()) {
            reportService.saveReport();
        }
        return reportService.addExpense(request);
    }

    @GetMapping("current-report/recommended-recalculations")
    @ResponseStatus(HttpStatus.OK)
    public EstimatedCostsRequest countRecommendations() {
        return reportService.countRecommendations();
    }
}
