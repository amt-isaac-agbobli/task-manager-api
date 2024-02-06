package com.isaac.taskmanagementapi.service.user;

import com.isaac.taskmanagementapi.dto.password.UpdatePasswordRequest;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        boolean passwordMatch = passwordEncoder.matches(request.getOldPassword(), user.getPassword());
        if (!passwordMatch) {
            throw new HttpException("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new HttpException("Passwords do not match", HttpStatus.BAD_REQUEST);
        }
        String newPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(newPassword);

        userRepository.save(user);

        return Map.of("message", "Password updated successfully");
    }





}
