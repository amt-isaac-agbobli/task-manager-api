package com.isaac.taskmanagementapi.service.task;

import com.isaac.taskmanagementapi.dto.Task.UpdateTaskRequest;
import com.isaac.taskmanagementapi.entity.Task;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UpdateTaskService {
    private final TaskRepository taskRepository;
    @Autowired
    public UpdateTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    public Object updateTask(UpdateTaskRequest request, int taskId, int userId) {

        Task taskExit = taskRepository.findById(taskId).orElse(null);
        if(taskExit == null)
            throw new HttpException("Task does not exist", HttpStatus.NOT_FOUND);

        if(taskExit.getCreatedBy().getId() != userId)
            throw new HttpException("You are not authorized to update this task",
                    HttpStatus.UNAUTHORIZED);

        taskExit.setTitle(request.getTitle());
        taskExit.setDescription(request.getDescription());
        taskExit.setDueDate(request.getDueDate());
        taskRepository.save(taskExit);

        return Map.of("message", "Task updated successfully");
    }
}
