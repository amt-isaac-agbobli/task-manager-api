package com.isaac.taskmanagementapi.service.password;

import com.isaac.taskmanagementapi.dto.password.UpdatePasswordRequest;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UpdatePasswordServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdatePasswordService updatePasswordService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void updateUserPasswordSuccessfully() {
        User user = new User();
        user.setPassword("oldPassword");
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setOldPassword("oldPassword");
        request.setNewPassword("newPassword");
        request.setConfirmPassword("newPassword");

        assertDoesNotThrow(() -> updatePasswordService.updateUserPassword("test@test.com", request));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void updateUserPasswordWithNonExistingUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setOldPassword("oldPassword");
        request.setNewPassword("newPassword");
        request.setConfirmPassword("newPassword");

        assertThrows(HttpException.class, () -> updatePasswordService.updateUserPassword("test@test.com", request));
    }

    @Test
    public void updateUserPasswordWithIncorrectOldPassword() {
        User user = new User();
        user.setPassword("oldPassword");
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setOldPassword("wrongOldPassword");
        request.setNewPassword("newPassword");
        request.setConfirmPassword("newPassword");

        assertThrows(HttpException.class, () -> updatePasswordService.updateUserPassword("test@test.com", request));
    }

    @Test
    public void updateUserPasswordWithNonMatchingNewPasswords() {
        User user = new User();
        user.setPassword("oldPassword");
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setOldPassword("oldPassword");
        request.setNewPassword("newPassword");
        request.setConfirmPassword("differentNewPassword");

        assertThrows(HttpException.class, () -> updatePasswordService.updateUserPassword("test@test.com", request));
    }
}