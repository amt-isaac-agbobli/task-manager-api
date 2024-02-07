package com.isaac.taskmanagementapi.controller.task;

import com.isaac.taskmanagementapi.dto.task.AddTaskRequest;
import com.isaac.taskmanagementapi.dto.task.TaskDto;
import com.isaac.taskmanagementapi.dto.task.UpdateTaskRequest;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.service.task.AddTaskService;
import com.isaac.taskmanagementapi.service.task.GetTaskService;
import com.isaac.taskmanagementapi.service.task.UpdateTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final GetTaskService getTaskService;
    @Autowired
    public TaskController(AddTaskService addTaskService,
                          UpdateTaskService updateTaskService,
                          GetTaskService getTaskService) {
        this.addTaskService = addTaskService;
        this.updateTaskService = updateTaskService;
        this.getTaskService = getTaskService;
    }

    @PostMapping("/add")
    @Operation(description = "Add a new task")
    public ResponseEntity<Object> addTask(@Valid @RequestBody AddTaskRequest request) {

        User user = authenticatedUser();
        return ResponseEntity.status(201).body(addTaskService.addTask(request, user));
    }

    @PutMapping("/update/{id}")
    @Operation(description = "Update a task")
    public ResponseEntity<Object> updateTask(@Valid @PathVariable("id") int id,
                                            @Valid @RequestBody UpdateTaskRequest request) {

        User user = authenticatedUser();
        return ResponseEntity.ok().body(updateTaskService.updateTask(request,id, user.getId()));
    }

    @PutMapping("/reassign-task/{id}")
    @Operation(description = "Reassign a task")
    public ResponseEntity<Object> updateTask(@Valid @PathVariable("id") int id,
                                            @Valid @RequestParam("assignedTo") int assignedTo) {
        User user = authenticatedUser();
        return ResponseEntity.ok().body(updateTaskService.reassignTask(id, user.getId(), assignedTo));
    }

    @PutMapping("/update-status/{id}")
    @Operation(description = "Update a task status")
    public ResponseEntity<Object> updateTaskStatus(@Valid @PathVariable("id") int id,
                                                  @Valid @RequestParam("status") String status) {
        User user = authenticatedUser();
        return ResponseEntity.ok().body(updateTaskService.updateTaskStatus(id, user.getId(), status));
    }
    @GetMapping("/my-tasks")
    @Operation(description = "Get tasks created by user")
    public ResponseEntity<Page<TaskDto>> getMyTasks(@Valid Pageable pageable) {
        User user = authenticatedUser();
        return ResponseEntity.ok().body(getTaskService.getTasksCreatedByUser(user, pageable));
    }

    @GetMapping("/assigned-tasks")
    @Operation(description = "Get tasks assigned to user")
    public ResponseEntity<Page<TaskDto>> getAssignedTasks(@Valid Pageable pageable) {
        User user = authenticatedUser();
        return ResponseEntity.ok().body(getTaskService.getTasksAssignedToUser(user, pageable));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = "Delete a task")
    public ResponseEntity<Object> deleteTask(@Valid @PathVariable("id") int id) {
        User user = authenticatedUser();
        return ResponseEntity.ok().body(updateTaskService.deleteTask(id, user.getId()));
    }

    private User authenticatedUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        return (User) authentication.getPrincipal();
    }
}
