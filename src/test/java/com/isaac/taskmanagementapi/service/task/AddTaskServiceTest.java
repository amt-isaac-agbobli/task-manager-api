package com.isaac.taskmanagementapi.service.task;

import com.isaac.taskmanagementapi.dto.task.AddTaskRequest;
import com.isaac.taskmanagementapi.entity.Task;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.TaskRepository;
import com.isaac.taskmanagementapi.repository.UserRepository;
import com.isaac.taskmanagementapi.util.email.interfaces.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddTaskServiceTest {

    @InjectMocks
    private AddTaskService addTaskService;

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
    void addTaskSuccessfully() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@test.com");

        AddTaskRequest request = new AddTaskRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");
        request.setDueDate(LocalDate.now());
        request.setAssignedTo(1);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(null);

        Object response = addTaskService.addTask(request, user);

        verify(taskRepository, times(1)).save(any(Task.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());

        assertNotNull(response);
    }

    @Test
    void addTaskWithNonExistentUser() {
        User user = new User();
        user.setId(1);

        AddTaskRequest request = new AddTaskRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");
        request.setDueDate(LocalDate.now());
        request.setAssignedTo(2);

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(HttpException.class, () -> {
            addTaskService.addTask(request, user);
        });

        assertEquals("User you are trying to assign the task to does not exist", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ((HttpException) exception).getHttpStatus());

        verify(taskRepository, times(0)).save(any(Task.class));
        verify(emailService, times(0)).sendEmail(anyString(), anyString(), anyString());
    }
}