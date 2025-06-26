package com.example.demo.entity;

import com.example.demo.entity.enums.ProductionType;
import com.example.demo.entity.enums.State;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(nullable = false)
    @Size(max = 25)
    private String instruction;

    @Size(max = 250)
    private String description;

    @OneToOne
    private Employee notifier;

    @OneToOne
    private Employee transferee;

    @Column(nullable = false)
    @ManyToMany
    private List<Employee> consignees;

    @Column(nullable = false)
    private LocalDateTime creationTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductionType productionType;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @OneToOne
    private Employee endEmployee;

    public Notification(String instruction, String description, Employee notifier, List<Employee> consignees,
                        ProductionType productionType) {
        this.state = State.REPORTED;
        this.instruction = instruction;
        this.description = description;
        this.notifier = notifier;
        this.transferee = null;
        this.consignees = consignees;
        this.creationTime = LocalDateTime.now();
        this.productionType = productionType;
    }
}
