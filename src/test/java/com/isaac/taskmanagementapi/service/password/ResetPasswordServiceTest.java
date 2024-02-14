package com.isaac.taskmanagementapi.service.password;

import com.isaac.taskmanagementapi.dto.password.ResetPasswordRequest;
import com.isaac.taskmanagementapi.entity.ResetPasswordToken;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.PasswordRepository;
import com.isaac.taskmanagementapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ResetPasswordServiceTest {

    @Mock
    private PasswordRepository passwordRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ResetPasswordService resetPasswordService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void resetPasswordSuccessfully() {
//        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
//        resetPasswordToken.setExpiryDate(new Date(System.currentTimeMillis() + 10000));
//        resetPasswordToken.setEmail("test@test.com");
//        when(passwordRepository.findByToken(anyString())).thenReturn(resetPasswordToken);
//
//        User user = new User();
//        when(userRepository.findByEmail(anyString())).thenReturn(user);
//        doNothing().when(userRepository).save(any(User.class));
//
//        ResetPasswordRequest request = new ResetPasswordRequest();
//        request.setPassword("newPassword");
//
//        Map<String, String> response = (Map<String, String>) resetPasswordService
//                .resetPassword("validToken", request);
//
//        assertEquals("Password reset successfully", response.get("message"));
//        verify(passwordRepository, times(1)).deleteById(anyInt());
//    }

    @Test
    public void resetPasswordWithInvalidToken() {
        when(passwordRepository.findByToken(anyString())).thenReturn(null);

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setPassword("newPassword");

        assertThrows(HttpException.class, () -> resetPasswordService.resetPassword("invalidToken", request));
    }

    @Test
    public void resetPasswordWithExpiredToken() {
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setExpiryDate(new Date(System.currentTimeMillis() - 10000));
        resetPasswordToken.setEmail("test@test.com");
        when(passwordRepository.findByToken(anyString())).thenReturn(resetPasswordToken);

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setPassword("newPassword");

        assertThrows(HttpException.class, () -> resetPasswordService.resetPassword("expiredToken", request));
    }
}