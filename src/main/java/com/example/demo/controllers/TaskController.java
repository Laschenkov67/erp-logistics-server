package com.example.demo.controllers;

import com.example.demo.dto.TaskDTO;
import com.example.demo.payload.request.TaskRequest;
import com.example.demo.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO getOneTask(@PathVariable("id") Long id) {
        return taskService.getOneTask(id);
    }

    @GetMapping("/kanban/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> getTasksByAssignee(@PathVariable("id") Long id) {
        return taskService.getTasksByAssignee(id);
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO addOneTask(@RequestBody TaskRequest taskRequest) {
        return taskService.addOneTask(taskRequest);
    }

    @PutMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO setNextCategory(@PathVariable("id") Long id) {
        return taskService.setNextCategory(id);
    }

    @PutMapping("/tasks/{id}/assign")
    public HttpStatus assignToMe(@PathVariable("id") Long id) {
        taskService.assignToMe(id);
        return HttpStatus.OK;
    }
}
