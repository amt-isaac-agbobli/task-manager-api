package com.isaac.taskmanagementapi.service.auth;

import com.isaac.taskmanagementapi.dto.auth.SignInRequest;
import com.isaac.taskmanagementapi.dto.auth.SignUpUserRequest;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.UserRepository;
import com.isaac.taskmanagementapi.service.user.UserService;
import com.isaac.taskmanagementapi.util.jwt.CreateJwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CreateJwtService createJwtService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void signUpSuccessfully() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        SignUpUserRequest request = new SignUpUserRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");
        request.setName("test");

        User response = (User) authService.signUp(request);

        assertEquals("test@test.com", response.getEmail());
        assertNull(response.getPassword());
        assertEquals("test", response.getName());
    }

    @Test
    public void signUpWithExistingUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(new User());

        SignUpUserRequest request = new SignUpUserRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");
        request.setName("test");

        assertThrows(HttpException.class, () -> authService.signUp(request));
    }

    @Test
    public void signInSuccessfully() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(createJwtService.execute(any(User.class))).thenReturn("token");

        SignInRequest request = new SignInRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");

        Map<String, String> response = (Map<String, String>) authService.signIn(request);

        assertEquals("token", response.get("token"));
    }

    @Test
    public void signInWithNonExistingUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        SignInRequest request = new SignInRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");

        assertThrows(HttpException.class, () -> authService.signIn(request));
    }

    @Test
    public void signInWithInvalidCredentials() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        SignInRequest request = new SignInRequest();
        request.setEmail("test@test.com");
        request.setPassword("wrongPassword");

        assertThrows(HttpException.class, () -> authService.signIn(request));
    }
}
