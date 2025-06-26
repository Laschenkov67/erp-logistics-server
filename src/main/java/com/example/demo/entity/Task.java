package com.example.demo.entity;

import com.example.demo.entity.enums.Category;
import com.example.demo.entity.enums.ProductionType;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(max = 25)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category = Category.TO_DO;

    @ElementCollection
    private List<Long> precedingTaskIds = new ArrayList<>();

    @OneToOne
    private Employee author;

    @OneToOne
    private Employee assignee;

    @Column(nullable = false)
    private LocalDateTime creationTime;

    @Column(nullable = false)
    private Integer estimatedTime;

    @Column(nullable = false)
    private LocalDateTime deadline;

    private LocalDateTime scheduledTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Size(max = 250)
    private String details;

    @OneToOne
    private Employee startEmployee;

    @OneToOne
    private Employee endEmployee;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductionType type;

    public Task(String name, List<Long> precedingTaskIds, Employee author, Employee assignee,
                LocalDateTime scheduledTime, Integer estimatedTime, LocalDateTime deadline, String details, ProductionType type) {
        this.name = name;
        this.precedingTaskIds = precedingTaskIds;
        this.author = author;
        this.assignee = assignee;
        this.creationTime = LocalDateTime.now();
        this.scheduledTime = scheduledTime;
        this.estimatedTime = estimatedTime;
        this.deadline = deadline;
        this.details = details;
        this.type = type;
    }

    public static int compare(Task t1, Task t2) {
        Integer sizePrecedingTasksFirst = t1.getPrecedingTaskIds().size();
        Integer sizePrecedingTasksSecond = t2.getPrecedingTaskIds().size();
        int comparison = sizePrecedingTasksFirst.compareTo(sizePrecedingTasksSecond);

        if (comparison == 0) {
            comparison = t1.getEstimatedTime().compareTo(t2.getEstimatedTime());
        }

        return comparison;
    }
}
