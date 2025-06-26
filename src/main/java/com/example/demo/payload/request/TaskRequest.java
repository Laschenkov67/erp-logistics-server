package com.example.demo.payload.request;

import com.example.demo.entity.enums.ProductionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    @NonNull
    private String name;
    private List<Long> precedingTaskIds;
    private Long assigneeId;
    @NonNull
    private Integer estimatedTime;
    @NonNull
    private LocalDateTime deadline;
    private LocalDateTime scheduledTime;
    private String details;
    @NonNull
    private ProductionType type;
}
