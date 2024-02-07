package com.isaac.taskmanagementapi.controller.task;

import com.isaac.taskmanagementapi.dto.Task.AddTaskRequest;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.service.task.AddTaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/tasks")
@SecurityRequirement(name = "bearer-key")
public class TaskController {
    private final AddTaskService addTaskService;

    public TaskController(AddTaskService addTaskService) {
        this.addTaskService = addTaskService;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addTask(@Valid @RequestBody AddTaskRequest request) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(201).body(addTaskService.addTask(request, user));
    }
}
