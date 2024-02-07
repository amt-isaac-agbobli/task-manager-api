package com.isaac.taskmanagementapi.service.user;

import com.isaac.taskmanagementapi.dto.password.UpdatePasswordRequest;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.UserRepository;
import com.isaac.taskmanagementapi.service.user.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService implements IUserService {
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







}
