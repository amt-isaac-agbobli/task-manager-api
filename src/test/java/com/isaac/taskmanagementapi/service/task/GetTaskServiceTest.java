package com.isaac.taskmanagementapi.service.task;

import com.isaac.taskmanagementapi.dto.task.TaskDto;
import com.isaac.taskmanagementapi.entity.Task;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GetTaskServiceTest {

    @InjectMocks
    private GetTaskService getTaskService;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

 @Test
public void getTasksCreatedByUserSuccessfully() {
    User user = new User();
    user.setId(1);

    Task task = new Task();
    task.setId(1);
    task.setTitle("Test Task");
    task.setDescription("Test Description");
    task.setDueDate(LocalDate.now());
    task.setCreatedBy(user);
    task.setAssignedTo(user); // Assign the user to the task

    Page<Task> tasks = new PageImpl<>(Arrays.asList(task));

    when(taskRepository.findByCreatedBy(any(User.class), any(PageRequest.class))).thenReturn(tasks);

    Page<TaskDto> response = getTaskService.getTasksCreatedByUser(user, PageRequest.of(0, 10));

    assertNotNull(response);
    assertEquals(1, response.getTotalElements());
}

    @Test
    public void getTasksAssignedToUserSuccessfully() {
        User user = new User();
        user.setId(1);

        Task task = new Task();
        task.setId(1);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.now());
        task.setCreatedBy(user);
        task.setAssignedTo(user);

        Page<Task> tasks = new PageImpl<>(Arrays.asList(task));

        when(taskRepository.findByAssignedTo(any(User.class), any(PageRequest.class))).thenReturn(tasks);

        Page<TaskDto> response = getTaskService.getTasksAssignedToUser(user, PageRequest.of(0, 10));

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    public void getTaskByIdSuccessfully() {
        User user = new User();
        user.setId(1);

        Task task = new Task();
        task.setId(1);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.now());
        task.setCreatedBy(user);
        task.setAssignedTo(user);

        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));

        TaskDto response = getTaskService.getTaskById(1, user);

        assertNotNull(response);
        assertEquals(1, response.getId());
    }

    @Test
    public void getTaskByIdUnauthorized() {
        User user = new User();
        user.setId(1);

        User otherUser = new User();
        otherUser.setId(2);

        Task task = new Task();
        task.setId(1);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.now());
        task.setCreatedBy(otherUser);
        task.setAssignedTo(otherUser);

        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));

        Exception exception = assertThrows(HttpException.class, () -> {
            getTaskService.getTaskById(1, user);
        });

        assertEquals("You are not authorized to view this task", exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, ((HttpException) exception).getHttpStatus());
    }
}