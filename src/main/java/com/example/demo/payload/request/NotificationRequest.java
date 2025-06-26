package com.example.demo.payload.request;

import com.example.demo.entity.enums.ProductionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    @NonNull
    private String instruction;
    private String description;
    @NonNull
    private List<Long> consigneeIds;
    @NonNull
    private ProductionType type;
}
