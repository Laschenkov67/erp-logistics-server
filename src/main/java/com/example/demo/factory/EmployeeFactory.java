package com.example.demo.factory;

import com.example.demo.entity.enums.Role;
import com.example.demo.payload.request.ContractRequest;
import com.example.demo.payload.request.EmployeeRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EmployeeFactory {

    private final String[] FIRST_NAMES = {"Ольга", "Иван", "Дмитрий",
            "Алексей", "Екатерина", "Анна", "Юлия", "Сергей",
            "Константин", "Александр", "Мария", "Наталья", "Михаил"};

    private final String[] LAST_NAMES = {"Иванов", "Петров", "Сидоров",
            "Кузнецов", "Смирнов", "Васильев", "Павлов"};
    private final Random r;

    public EmployeeFactory() {
        r = new Random();
    }

    private String generate(String[] table) {
        return table[r.nextInt(table.length)];
    }

    private String generateEmail(String firstName, String lastName) {
        return firstName.toLowerCase() + "." + lastName.toLowerCase() + r.nextInt() + "@domain.com";
    }

    public Role generateRole() {
        return Role.values()[r.nextInt(Role.values().length)];
    }

    private Role generateAdminRole() {
        List<Role> roles = Arrays.asList(Role.values());
        List<Role> adminRoles = roles.stream()
                .filter(role -> role.name().contains("ADMIN"))
                .collect(Collectors.toList());
        return adminRoles.get(r.nextInt(adminRoles.size()));
    }

    private Role generateNonAdminRole() {
        List<Role> roles = Arrays.asList(Role.values());
        List<Role> nonAdminRoles = roles.stream()
                .filter(role -> !role.name().contains("ADMIN"))
                .collect(Collectors.toList());
        return nonAdminRoles.get(r.nextInt(nonAdminRoles.size()));
    }

    private ContractRequest generateContractRequest() {
        StringBuilder accountNumberBuilder = new StringBuilder();
        for(int i = 0; i < 26; i++) {
            accountNumberBuilder.append(r.nextInt(10));
        }
        return new ContractRequest(accountNumberBuilder.toString(), r.nextInt(7)+20,
                r.nextDouble()+2000.00);
    }

    public EmployeeRequest generateEmployeeRequestWithRole(Role role) {
        String firstName = generate(FIRST_NAMES);
        String lastName = generate(LAST_NAMES);
        return new EmployeeRequest(firstName, lastName, generateEmail(firstName, lastName),
                role, generateContractRequest());
    }
}
