package com.isaac.taskmanagementapi.service;

import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
