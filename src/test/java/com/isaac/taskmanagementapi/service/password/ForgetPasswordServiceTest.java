package com.isaac.taskmanagementapi.service.password;

import com.isaac.taskmanagementapi.dto.password.ForgetPasswordRequest;
import com.isaac.taskmanagementapi.entity.ResetPasswordToken;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.PasswordRepository;
import com.isaac.taskmanagementapi.repository.UserRepository;
import com.isaac.taskmanagementapi.util.email.interfaces.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ForgetPasswordServiceTest {

    @Mock
    private PasswordRepository passwordRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ForgetPasswordService forgetPasswordService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void forgotPasswordSuccessfully() {
        User user = new User();
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(passwordRepository.findByEmail(anyString())).thenReturn(null);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        ForgetPasswordRequest request = new ForgetPasswordRequest();
        request.setEmail("test@test.com");

        Map<String, String> response = (Map<String, String>) forgetPasswordService.forgotPassword(request);

        assertEquals("Email sent successfully", response.get("message"));
        verify(passwordRepository, times(1)).save(any(ResetPasswordToken.class));
    }

    @Test
    void forgotPasswordWithNonRegisteredUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        ForgetPasswordRequest request = new ForgetPasswordRequest();
        request.setEmail("test@test.com");

        assertThrows(HttpException.class, () -> forgetPasswordService.forgotPassword(request));
    }

    @Test
    void forgotPasswordWithExistingResetToken() {
        User user = new User();
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setEmail("test@test.com");
        resetPasswordToken.setToken("existingToken");
        resetPasswordToken.setExpiryDate(new Date());
        when(passwordRepository.findByEmail(anyString())).thenReturn(resetPasswordToken);

        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        ForgetPasswordRequest request = new ForgetPasswordRequest();
        request.setEmail("test@test.com");

        Map<String, String> response = (Map<String, String>) forgetPasswordService.forgotPassword(request);

        assertEquals("Email sent successfully", response.get("message"));
        verify(passwordRepository, times(1)).save(any(ResetPasswordToken.class));
    }
}
