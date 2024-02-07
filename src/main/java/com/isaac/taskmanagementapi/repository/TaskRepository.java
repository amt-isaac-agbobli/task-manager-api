package com.isaac.taskmanagementapi.repository;

import com.isaac.taskmanagementapi.entity.Task;
import com.isaac.taskmanagementapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository  extends JpaRepository<Task, Integer> {
    Page<Task> findByCreatedBy(User user, Pageable pageable);
}
