package com.isaac.taskmanagementapi.controller.task;

import com.isaac.taskmanagementapi.dto.task.AddTaskRequest;
import com.isaac.taskmanagementapi.dto.task.TaskDto;
import com.isaac.taskmanagementapi.dto.task.UpdateTaskRequest;
import com.isaac.taskmanagementapi.entity.Task;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.service.task.AddTaskService;
import com.isaac.taskmanagementapi.service.task.GetTaskService;
import com.isaac.taskmanagementapi.service.task.UpdateTaskService;
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
    public ResponseEntity<Object> addTask(@Valid @RequestBody AddTaskRequest request) {

        User user = authenticatedUser();
        return ResponseEntity.status(201).body(addTaskService.addTask(request, user));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable("id") int id,
                                            @Valid @RequestBody UpdateTaskRequest request) {

        User user = authenticatedUser();
        return ResponseEntity.ok().body(updateTaskService.updateTask(request,id, user.getId()));
    }

    @PutMapping("/reassign-task/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable("id") int id,
                                            @Valid @RequestParam("assignedTo") int assignedTo) {
        User user = authenticatedUser();
        return ResponseEntity.ok().body(updateTaskService.reassignTask(id, user.getId(), assignedTo));
    }
    @GetMapping("/my-tasks")
    public ResponseEntity<Page<TaskDto>> getMyTasks(Pageable pageable) {
        User user = authenticatedUser();
        return ResponseEntity.ok().body(getTaskService.getTasksCreatedByUser(user, pageable));
    }

    @GetMapping("/assigned-tasks")
    public ResponseEntity<Page<TaskDto>> getAssignedTasks(Pageable pageable) {
        User user = authenticatedUser();
        return ResponseEntity.ok().body(getTaskService.getTasksAssignedToUser(user, pageable));
    }

    private User authenticatedUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        return (User) authentication.getPrincipal();
    }
}
