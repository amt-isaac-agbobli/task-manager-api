package com.isaac.taskmanagementapi.service.task;

import com.isaac.taskmanagementapi.dto.task.UpdateTaskRequest;
import com.isaac.taskmanagementapi.entity.Task;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.enums.Status;
import com.isaac.taskmanagementapi.repository.TaskRepository;
import com.isaac.taskmanagementapi.repository.UserRepository;
import com.isaac.taskmanagementapi.util.email.interfaces.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateTaskServiceTest {

    @InjectMocks
    private UpdateTaskService updateTaskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateTaskSuccessfully() {
        User user = new User();
        user.setId(1);

        Task task = new Task();
        task.setId(1);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.now());
        task.setCreatedBy(user);

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Updated Task");
        request.setDescription("Updated Description");
        request.setDueDate(LocalDate.now().plusDays(1));

        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));

        Object response = updateTaskService.updateTask(request, 1, 1);

        verify(taskRepository, times(1)).save(any(Task.class));

        assertNotNull(response);
    }

    @Test
    void updateTaskStatusSuccessfully() {
        User user = new User();
        user.setId(1);

        Task task = new Task();
        task.setId(1);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.now());
        task.setCreatedBy(user);
        task.setStatus(Status.PENDING);

        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));

        Object response = updateTaskService.updateTaskStatus(1, 1, "COMPLETED");

        verify(taskRepository, times(1)).save(any(Task.class));

        assertNotNull(response);
    }

    @Test
    void reassignTaskSuccessfully() {
        User user = new User();
        user.setId(1);

        User assignee = new User();
        assignee.setId(2);
        assignee.setEmail("test@example.com");

        Task task = new Task();
        task.setId(1);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.now());
        task.setCreatedBy(user);
        task.setAssignedTo(user);

        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(assignee));

        Object response = updateTaskService.reassignTask(1, 1, 2);

        verify(taskRepository, times(1)).save(any(Task.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());

        assertNotNull(response);
    }

    @Test
    void deleteTaskSuccessfully() {
        User user = new User();
        user.setId(1);

        Task task = new Task();
        task.setId(1);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.now());
        task.setCreatedBy(user);

        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));

        Object response = updateTaskService.deleteTask(1, 1);

        verify(taskRepository, times(1)).delete(any(Task.class));

        assertNotNull(response);
    }
}