package com.isaac.taskmanagementapi.service.auth;

import com.isaac.taskmanagementapi.dto.auth.SignInRequest;
import com.isaac.taskmanagementapi.dto.auth.SignUpUserRequest;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.UserRepository;
import com.isaac.taskmanagementapi.service.auth.interfaces.IAuthService;
import com.isaac.taskmanagementapi.util.jwt.CreateJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final CreateJwtService createJWTService;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       CreateJwtService createJWTService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.createJWTService = createJWTService;

    }

    public Object signUp(SignUpUserRequest user) {
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
        User userCreated = userRepository.save(newUser);

        userCreated.setPassword(null);
        return userCreated;
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
        String token = createJWTService.execute(userExists);

        return Map.of("token", token);
    }
}
