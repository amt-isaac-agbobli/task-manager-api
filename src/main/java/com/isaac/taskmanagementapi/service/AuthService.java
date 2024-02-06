package com.isaac.taskmanagementapi.service;

import com.isaac.taskmanagementapi.dto.SignInRequest;
import com.isaac.taskmanagementapi.dto.SignUpUserRequest;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.UserRepository;
import com.isaac.taskmanagementapi.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthService(UserService userService,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;

    }

    public User signUp(SignUpUserRequest user) {
        User userExists = userRepository.findByEmail(user.getEmail());
        if (userExists != null) {
            throw new HttpException("User already exists", HttpStatus.CONFLICT);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User newUser = User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .build();
        return userRepository.save(newUser);
    }

    public Object signIn(SignInRequest user) {
        User userExists = userRepository.findByEmail(user.getEmail());
        if (userExists == null) {
            throw new HttpException("User does not exist", HttpStatus.NOT_FOUND);
        }
        boolean passwordMatch = passwordEncoder.matches(user.getPassword(), userExists.getPassword());
        if (!passwordMatch) {
            throw new HttpException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        String token = jwtTokenUtil.generateToken(userExists);

        return Map.of("token", token);
    }
}
