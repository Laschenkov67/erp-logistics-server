package com.example.demo.dto;

import com.example.demo.entity.Notification;
import com.example.demo.entity.enums.ProductionType;
import com.example.demo.entity.enums.State;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class NotificationDTO {
    private long id;
    private State state;
    private String instruction;
    private String description;
    private EmployeeDTO notifier;
    private EmployeeDTO transferee;
    private List<EmployeeDTO> consignees;
    private LocalDateTime creationTime;
    private ProductionType type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private EmployeeDTO endEmployee;

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.state = notification.getState();
        this.instruction = notification.getInstruction();
        this.description = notification.getDescription();
        this.notifier = new EmployeeDTO(notification.getNotifier());
        this.consignees = new ArrayList<>();
        notification.getConsignees().forEach(consignee -> this.consignees.add(new EmployeeDTO(consignee)));
        this.creationTime = notification.getCreationTime();

        if (notification.getStartTime() != null) {
            this.startTime = notification.getStartTime();
            this.transferee = new EmployeeDTO(notification.getTransferee());
        }

        if (notification.getEndTime() != null) {
            this.endTime = notification.getEndTime();
            this.endEmployee = new EmployeeDTO(notification.getEndEmployee());
        }
    }

    public NotificationDTO(String instruction, String description, EmployeeDTO notifier, List<EmployeeDTO> consignees,
                           ProductionType type) {
        this.instruction = instruction;
        this.description = description;
        this.notifier = notifier;
        this.consignees = consignees;
        this.type = type;
    }
}
