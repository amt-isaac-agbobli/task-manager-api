package com.isaac.taskmanagementapi.service.task;

import com.isaac.taskmanagementapi.dto.Task.TaskRequest;
import com.isaac.taskmanagementapi.entity.Task;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.enums.Status;
import com.isaac.taskmanagementapi.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class AddTaskService {
    private final TaskRepository taskRepository;

    public AddTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Object addTask(TaskRequest request, User user) {
        int assignedTo = request.getAssignedTo() == 0 ? user.getId() : request.getAssignedTo();

        Task task = Task.builder()
                     .title(request.getTitle())
                     .description(request.getDescription())
                     .dueDate(LocalDate.parse(request.getDueDate()))
                     .status(Status.PENDING)
                     .createdBy(user)
                     .assignedTo(User.builder().id(assignedTo).build())
                     .build();
                taskRepository.save(task);

        return Map.of( "message", "Task created successfully");
    }
}
