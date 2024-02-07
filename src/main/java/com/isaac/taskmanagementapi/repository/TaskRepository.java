package com.isaac.taskmanagementapi.repository;

import com.isaac.taskmanagementapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository  extends JpaRepository<Task, Integer> {
}
