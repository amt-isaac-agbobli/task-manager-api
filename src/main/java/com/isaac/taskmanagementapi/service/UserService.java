package com.isaac.taskmanagementapi.service;

import com.isaac.taskmanagementapi.dto.SignUpUserRequest;
import com.isaac.taskmanagementapi.dto.UpdatePasswordRequest;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserProfile(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new HttpException("User does not exist", HttpStatus.NOT_FOUND);
        }
        user.setPassword(null);
        return user;
    }

    public Object updateUserPassword(String email, UpdatePasswordRequest request) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new HttpException("User does not exist", HttpStatus.NOT_FOUND);
        }
        boolean passwordMatch = user.getPassword().equals(request.getOldPassword());
        if (!passwordMatch) {
            throw new HttpException("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new HttpException("Passwords do not match", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return Map.of("message", "Password updated successfully");
    }



}
