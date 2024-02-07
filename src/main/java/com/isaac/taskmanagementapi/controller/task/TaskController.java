package com.isaac.taskmanagementapi.controller.task;

import com.isaac.taskmanagementapi.dto.Task.AddTaskRequest;
import com.isaac.taskmanagementapi.dto.Task.UpdateTaskRequest;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.service.task.AddTaskService;
import com.isaac.taskmanagementapi.service.task.UpdateTaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tasks")
@SecurityRequirement(name = "bearer-key")
public class TaskController {
    private final AddTaskService addTaskService;
    private final UpdateTaskService updateTaskService;
    @Autowired
    public TaskController(AddTaskService addTaskService,
                          UpdateTaskService updateTaskService) {
        this.addTaskService = addTaskService;
        this.updateTaskService = updateTaskService;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addTask(@Valid @RequestBody AddTaskRequest request) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(201).body(addTaskService.addTask(request, user));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable("id") int id,
                                            @Valid @RequestBody UpdateTaskRequest request) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok().body(updateTaskService.updateTask(request,id, user.getId()));
    }
}
