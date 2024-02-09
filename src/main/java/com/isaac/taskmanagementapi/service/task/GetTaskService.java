package com.isaac.taskmanagementapi.service.task;

import com.isaac.taskmanagementapi.dto.task.TaskDto;
import com.isaac.taskmanagementapi.dto.user.UserDto;
import com.isaac.taskmanagementapi.entity.Task;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GetTaskService {
    private final TaskRepository taskRepository;
    @Autowired
    public GetTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
     @Cacheable(value = "tasks", key = "#user.id")
    public Page<TaskDto> getTasksCreatedByUser(User user, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByCreatedBy(user, pageable);
        return tasks.map(this::convertToDto);
    }
    @Cacheable(value = "tasks", key = "#user.id")
    public Page<TaskDto> getTasksAssignedToUser(User user, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByAssignedTo(user, pageable);
        return tasks.map(this::convertToDto);
    }

    @Cacheable(value = "task", key = "#user.id")
    public TaskDto getTaskById(int id, User user) {
        Task task = taskRepository.findById(id).orElse(null);
        if(task == null)
            return null;
        if(task.getCreatedBy().getId() != user.getId() && task.getAssignedTo().getId() != user.getId())
            throw new HttpException("You are not authorized to view this task", HttpStatus.FORBIDDEN);
        return convertToDto(task);
    }

    private TaskDto convertToDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());

        UserDto createdByDto = new UserDto();
        createdByDto.setId(task.getCreatedBy().getId());
        createdByDto.setEmail(task.getCreatedBy().getEmail());
        dto.setCreatedBy(createdByDto);

        UserDto assignedToDto = new UserDto();
        assignedToDto.setId(task.getAssignedTo().getId());
        assignedToDto.setEmail(task.getAssignedTo().getEmail());
        dto.setAssignedTo(assignedToDto);

        return dto;
    }




}
