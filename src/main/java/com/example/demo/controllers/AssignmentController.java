package com.example.demo.controllers;

import com.example.demo.dto.TaskDTO;
import com.example.demo.payload.request.AssignmentRequest;
import com.example.demo.services.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("/assignment")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> getTasksByAssigneeIsNull() {
        return assignmentService.getTasksByAssigneeIsNull();
    }

    @PutMapping("/assignment")
    @ResponseStatus(HttpStatus.OK)
    public HttpStatus assignToEmployees(@RequestBody AssignmentRequest assignmentRequest) {
        assignmentService.assignToEmployees(assignmentRequest);
        return HttpStatus.OK;
    }
}
