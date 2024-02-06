package com.isaac.taskmanagementapi.service;

import com.isaac.taskmanagementapi.dto.ForgetPasswordRequest;
import com.isaac.taskmanagementapi.entity.ResetPasswordToken;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.PasswordRepository;
import com.isaac.taskmanagementapi.repository.UserRepository;
import com.isaac.taskmanagementapi.util.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class ForgetPasswordPasswordService {
    private final PasswordRepository passwordRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    @Autowired
    public ForgetPasswordPasswordService(PasswordRepository passwordRepository,
                                         EmailService emailService,
                                         UserRepository userRepository) {
        this.passwordRepository = passwordRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }


    public Object forgotPassword(ForgetPasswordRequest forgetPasswordRequest) {
        User user = findUserByEmail(forgetPasswordRequest.getEmail());
        String token = generateToken();
        Date expiryDate = calculateExpiryDate();

        ResetPasswordToken resetPasswordToken = findOrCreateResetPasswordToken(forgetPasswordRequest.getEmail(), token, expiryDate);
        passwordRepository.save(resetPasswordToken);

        sendResetPasswordEmail(forgetPasswordRequest.getEmail(), token);

        return Map.of("message", "Email sent successfully");
    }

    private User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new HttpException("User is not registered in the system", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private Date calculateExpiryDate() {
        return Date.from(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")));
    }

    private ResetPasswordToken findOrCreateResetPasswordToken(String email, String token, Date expiryDate) {
        ResetPasswordToken resetPasswordToken = passwordRepository.findByEmail(email);
        if (resetPasswordToken == null) {
            resetPasswordToken = new ResetPasswordToken();
            resetPasswordToken.setEmail(email);
        }
        resetPasswordToken.setToken(token);
        resetPasswordToken.setExpiryDate(expiryDate);
        return resetPasswordToken;
    }

    private void sendResetPasswordEmail(String email, String token) {
        emailService.sendEmail(
                email,
                "Reset Password",
                "To reset your password, click the link below:\n" +
                        "http://localhost:8080/reset-password?token=" + token
        );
    }


}
