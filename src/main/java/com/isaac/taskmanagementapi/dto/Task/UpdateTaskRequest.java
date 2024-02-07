package com.isaac.taskmanagementapi.dto.Task;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTaskRequest {
    @NotBlank(message = "Title is required")
    @Length(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Length(min = 10, max = 1000, message = "Description must be between 3 and 1000 characters")
    private String description;

    private LocalDate dueDate;
    
}
