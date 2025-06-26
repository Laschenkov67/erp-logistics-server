package com.example.demo;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.*;
import com.example.demo.entity.enums.Role;
import com.example.demo.exceptions.InvalidRequestException;
import com.example.demo.payload.request.AdminRequest;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class SetupController {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder bcryptEncoder;
    private final TeamRepository teamRepository;
    private final DailyPlanRepository dailyPlanRepository;
    private final CurrentReportRepository currentReportRepository;
    private final EstimatedCostsRepository estimatedCostsRepository;
    private final ContractRepository contractRepository;

    @Autowired
    public SetupController(EmployeeRepository employeeRepository, BCryptPasswordEncoder bcryptEncoder,
                           TeamRepository teamRepository, DailyPlanRepository dailyPlanRepository,
                           CurrentReportRepository currentReportRepository,
                           EstimatedCostsRepository estimatedCostsRepository, ContractRepository contractRepository) {
        this.employeeRepository = employeeRepository;
        this.bcryptEncoder = bcryptEncoder;
        this.teamRepository = teamRepository;
        this.dailyPlanRepository = dailyPlanRepository;
        this.currentReportRepository = currentReportRepository;
        this.estimatedCostsRepository = estimatedCostsRepository;
        this.contractRepository = contractRepository;
    }

    @GetMapping("/check-setup")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInitialized() {
        return employeeRepository.findByRole(Role.ADMIN).isPresent();
    }

    @PostMapping("/setup-admin")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO setupAdmin(@RequestBody AdminRequest request) {
        Optional<List<Employee>> findAdmin = employeeRepository.findByRole(Role.ADMIN);
        if (findAdmin.isPresent() && !findAdmin.get().isEmpty()) {
            throw new InvalidRequestException("ADMIN already exists!");
        }
        Employee employee = request.extractUser();
        employee.encodePassword(bcryptEncoder.encode(employee.getPassword()));
        contractRepository.save(employee.getContract());
        employeeRepository.save(employee);
        return new EmployeeDTO(employee);
    }

    @PostMapping("/setup-teams")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus setupTeams() {
        List<Team> teams = teamRepository.findAll();
        if (!teams.isEmpty()) {
            throw new InvalidRequestException("Teams were already setup!");
        }
        Optional<List<Employee>> findAdmin = employeeRepository.findByRole(Role.ADMIN);
        if (!findAdmin.isPresent()) {
            throw new InvalidRequestException("The system needs an ADMIN first!");
        }
        Team admins = new Team(Role.ADMIN);
        admins.addEmployee(findAdmin.get().get(0));
        teamRepository.save(admins);
        teamRepository.save(new Team(Role.ACCOUNTANT));
        teamRepository.save(new Team(Role.ANALYST));
        teamRepository.save(new Team(Role.WAREHOUSE));
        return HttpStatus.CREATED;
    }

    @PostMapping("/setup-daily-plan")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus setupDailyPlan() {
        List<DailyPlan> findDailyPlans = dailyPlanRepository.findAll();
        if (!findDailyPlans.isEmpty()) {
            throw new InvalidRequestException("Daily plan has already been setup!");
        }
        dailyPlanRepository.save(new DailyPlan());
        return HttpStatus.CREATED;
    }

    @PostMapping("/setup-reports")
    @ResponseStatus(HttpStatus.CREATED)
    public CurrentReport setupReports() {
        List<EstimatedCosts> findEstimatedCosts = estimatedCostsRepository.findAll();
        List<CurrentReport> findCurrentReports = currentReportRepository.findAll();
        if (!findEstimatedCosts.isEmpty() || !findCurrentReports.isEmpty()) {
            throw new InvalidRequestException("Reports have already been setup!");
        }
        EstimatedCosts estimatedCosts = new EstimatedCosts();
        estimatedCostsRepository.save(estimatedCosts);
        CurrentReport currentReport = new CurrentReport(estimatedCosts);
        currentReportRepository.save(currentReport);

        return currentReport;
    }
}
