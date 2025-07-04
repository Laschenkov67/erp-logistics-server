package com.example.demo.services;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Holiday;
import com.example.demo.entity.Team;
import com.example.demo.entity.enums.Role;
import com.example.demo.exceptions.InvalidRequestException;
import com.example.demo.exceptions.NotAManagerException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payload.request.EmployeeRequest;
import com.example.demo.repository.ContractRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.HolidayRepository;
import com.example.demo.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ContractRepository contractRepository;
    private final TeamRepository teamRepository;
    private final BCryptPasswordEncoder bcryptEncoder;
    private final HolidayRepository holidayRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, ContractRepository contractRepository,
                           TeamRepository teamRepository, BCryptPasswordEncoder bcryptEncoder,
                           HolidayRepository holidayRepository) {
        this.employeeRepository = employeeRepository;
        this.contractRepository = contractRepository;
        this.teamRepository = teamRepository;
        this.bcryptEncoder = bcryptEncoder;
        this.holidayRepository = holidayRepository;
    }

    public void checkIfCanBeAdded(EmployeeRequest request) {
        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new InvalidRequestException("This username (email) is already taken!");
        }
        if (request.getRole().name().contains("ADMIN") &&
                employeeRepository.findByRole(request.getRole()).isPresent()) {
            throw new InvalidRequestException("There can only be one " + request
                    .getRole().name());
        }
    }

    public void saveEmployee(Employee employee) {
        contractRepository.save(employee.getContract());
        employeeRepository.save(employee);
        addToTeam(employee);
    }

    public void checkIfEmployeeExists(long id) {
        if (!employeeRepository.findById(id).isPresent()) {
            throw new NotFoundException("Such employees doesn't exist!");
        }
    }

    public void removeEmployee(Employee employee) {
        removeFromTeam(employee);
        removeHolidays(employee);
        employeeRepository.delete(employee);
        contractRepository.delete(employee.getContract());
    }

    private void removeHolidays(Employee employee) {
        Optional<List<Holiday>> byEmployeeId = holidayRepository.findByEmployeeId(employee.getId());
        byEmployeeId.ifPresent(holidays -> holidays.forEach(holidayRepository::delete));
    }

    public void checkIfIsManager(Long id) {
        if (!employeeRepository.findById(id)
                .orElseThrow(NotFoundException::new)
                .isManager()) {
            throw new NotAManagerException("This employee is not a manager and therefore can't have subordinates!");
        }
    }

    public List<EmployeeDTO> getSubordinates(long id) {
        Optional<Team> team = teamRepository.findByManagerId(id);
        if (!team.isPresent()) {
            return new ArrayList<>();
        }
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        team.get().getEmployees().forEach(employee -> employeeDTOS.add(new EmployeeDTO(employee)));
        return employeeDTOS;
    }

    public void validatePassword(long id, String password) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        if (employee.isPasswordValid()) {
            throw new InvalidRequestException("This employee has already validated their password!");
        }
        employee.changePassword(bcryptEncoder.encode(password));
        employeeRepository.save(employee);
    }

    private void removeFromTeam(Employee employee) {
        Role role = employee.isManager() ? mapToNoAdmin(employee.getRole()) : employee.getRole();
        Team team = teamRepository.findByRole(role);
        if (employee.isManager()) {
            if (employee.getRole().equals(Role.ADMIN)) {
                Team adminTeam = teamRepository.findByRole(Role.ADMIN);
                adminTeam.removeManager();
                teamRepository.save(adminTeam);
            } else {
                Team adminTeam = teamRepository.findByRole(Role.ADMIN);
                adminTeam.removeEmployee(employee);
                teamRepository.save(adminTeam);
                team.removeManager();
                teamRepository.save(team);
            }
        } else {
            team.removeEmployee(employee);
            teamRepository.save(team);
        }
    }

    private void addToTeam(Employee employee) {
        Role role = employee.getRole();
        Team team;
        if (role.name().contains("ADMIN")) {
            Team adminTeam = teamRepository.findByRole(Role.ADMIN);
            adminTeam.addEmployee(employee);
            teamRepository.save(adminTeam);
            Role newRole = mapToNoAdmin(role);
            team = teamRepository.findByRole(newRole);
            team.addEmployee(employee);
        } else {
            team = teamRepository.findByRole(role);
            team.addEmployee(employee);
        }
        teamRepository.save(team);
    }

    private Role mapToNoAdmin(Role role) {
        switch (role) {
            case ADMIN_ACCOUNTANT:
                return Role.ACCOUNTANT;
            case ADMIN_ANALYST:
                return Role.ANALYST;
            case ADMIN_WAREHOUSE:
                return Role.WAREHOUSE;
            case ADMIN:
                return Role.ADMIN;
        }
        return role;
    }

    public List<EmployeeDTO> mapToDtos(List<Employee> employees) {
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        employees.forEach(employee -> employeeDTOS.add(new EmployeeDTO(employee)));
        return employeeDTOS;
    }

    public List<EmployeeDTO> filterByPrivilege(List<EmployeeDTO> employeeDTOS, String privilege) {
        if (privilege.equalsIgnoreCase("admin")) {
            return employeeDTOS.stream()
                    .filter(EmployeeDTO::isManager)
                    .collect(Collectors.toList());
        } else if (privilege.equalsIgnoreCase("user")) {
            return employeeDTOS.stream()
                    .filter(employee -> !employee.isManager())
                    .collect(Collectors.toList());
        }
        return employeeDTOS;
    }

    public void checkIfUserLoggedIn(long id) throws AccessDeniedException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        if (!username.equals(employee.getEmail())) {
            throw new AccessDeniedException("User is not logged in!");
        }
    }
}
