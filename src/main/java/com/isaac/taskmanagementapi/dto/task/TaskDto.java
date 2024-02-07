package com.isaac.taskmanagementapi.dto.task;

import com.isaac.taskmanagementapi.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {
    private int id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private UserDto createdBy;
    private UserDto assignedTo;

}