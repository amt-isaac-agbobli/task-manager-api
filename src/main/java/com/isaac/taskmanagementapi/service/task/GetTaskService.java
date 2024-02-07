package com.isaac.taskmanagementapi.service.task;

import com.isaac.taskmanagementapi.repository.TaskRepository;
import com.isaac.taskmanagementapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    @Autowired
    public GetTaskService(TaskRepository taskRepository,
                          UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }


}
