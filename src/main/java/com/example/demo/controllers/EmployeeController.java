package com.example.demo.controllers;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Contract;
import com.example.demo.entity.Employee;
import com.example.demo.entity.enums.Role;
import com.example.demo.exceptions.ForbiddenException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payload.request.EmployeeRequest;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.services.EmailService;
import com.example.demo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final String EMPLOYEE_NOT_FOUND = "Such employee doesn't exist!";

    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bcryptEncoder;
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository, EmailService emailService,
                              BCryptPasswordEncoder bcryptEncoder, EmployeeService employeeService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
        this.bcryptEncoder = bcryptEncoder;
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeDTO> getAllEmployees(@RequestParam(value = "privilege", required = false) String privilege) {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOS = employeeService.mapToDtos(employees);
        if (privilege != null) {
            employeeDTOS = employeeService.filterByPrivilege(employeeDTOS, privilege);
        }
        return employeeDTOS;
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO addNewEmployee(@RequestBody EmployeeRequest request) {
        Employee employee = request.extractUser();
        employeeService.checkIfCanBeAdded(request);
        emailService.sendMessage(employee.getEmail(), "Your first login password",
                Collections.singletonList("Your automatically generated password is: " +
                        employee.getPassword() +
                        ". You will be prompt to change it after your first login attempt."));
        employee.encodePassword(bcryptEncoder.encode(employee.getPassword()));
        employeeService.saveEmployee(employee);
        return new EmployeeDTO(employee);
    }

    @GetMapping("/employees/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getOneEmployee(@PathVariable("id") long id) {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Employee employee = employeeRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND));
        if (employee.getId() != id && employee.getRole() != Role.ADMIN) {
            throw new ForbiddenException();
        }
        return new UserDTO(employeeRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND))
        );
    }

    @DeleteMapping("/employees/{id}")
    public HttpStatus fireEmployee(@PathVariable("id") long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND));
        employeeService.removeEmployee(employee);
        return HttpStatus.OK;
    }

    @GetMapping("/employees/{id}/subordinates")
    public List<EmployeeDTO> getSubordinates(@PathVariable("id") long id) {
        employeeService.checkIfEmployeeExists(id);
        employeeService.checkIfIsManager(id);
        return employeeService.getSubordinates(id);
    }

    @GetMapping("/employees/colleagues")
    public List<EmployeeDTO> getColleagues() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOS = employeeService.mapToDtos(employees);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Employee author = employeeRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND));

        return employeeDTOS.stream()
                .filter(employeeDTO -> !employeeDTO.getEmail().equals(author.getEmail()))
                .sorted(Comparator.comparing(EmployeeDTO::getRole))
                .collect(Collectors.toList());
    }

    @PostMapping("/employees/{id}/validate-password")
    public HttpStatus validatePassword(@PathVariable("id") long id, @RequestBody String password) {
        employeeService.checkIfEmployeeExists(id);
        employeeService.validatePassword(id, password);
        return HttpStatus.NO_CONTENT;
    }

    @GetMapping("/logged-in-user")
    public Employee getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        if (!employeeRepository.findByEmail(username).isPresent()) {
            throw new NotFoundException("There is no such user!");
        }
        return employeeRepository.findByEmail(username).get();
    }

    @GetMapping("/profiles/{id}")
    public EmployeeDTO getProfile(@PathVariable("id") long id) {
        return new EmployeeDTO(employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND))
        );
    }

    @GetMapping("profiles/{id}/contract")
    public Contract getContract(@PathVariable("id") long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND))
                .getContract();
    }
}
