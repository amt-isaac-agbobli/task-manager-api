package com.isaac.taskmanagementapi.service.task;

import com.isaac.taskmanagementapi.dto.Task.AddTaskRequest;
import com.isaac.taskmanagementapi.entity.Task;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.enums.Status;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.TaskRepository;
import com.isaac.taskmanagementapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class AddTaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public AddTaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Object addTask(AddTaskRequest request, User user) {
        int assignedTo = request.getAssignedTo() == 0 ? user.getId() : request.getAssignedTo();

        assignee(assignedTo);

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

    private void assignee(int assignedTo) {
        userRepository.findById(assignedTo)
                .orElseThrow(() -> new HttpException("User you are trying to assign the task to does not exist",
                        HttpStatus.NOT_FOUND));
    }
}
