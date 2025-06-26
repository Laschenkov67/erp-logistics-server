package com.example.demo.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class EmailEntityRequest {
    private String email;
    @NotNull
    private String subject;
    @NotNull
    private List<String> content;

}
