package com.isaac.taskmanagementapi.service.password;

import com.isaac.taskmanagementapi.dto.password.ResetPasswordRequest;
import com.isaac.taskmanagementapi.entity.ResetPasswordToken;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.PasswordRepository;
import com.isaac.taskmanagementapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class ResetPasswordService {
    private final PasswordRepository passwordRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public ResetPasswordService(PasswordRepository passwordRepository,
                                UserRepository userRepository,
                                PasswordEncoder passwordEncoder) {
        this.passwordRepository = passwordRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    public Object resetPassword(String token, ResetPasswordRequest resetPasswordRequest) {
        validateToken(token);
        ResetPasswordToken resetPasswordToken = findResetPasswordToken(token);
        validateTokenExpiry(resetPasswordToken);
        User user = findUserByEmail(resetPasswordToken.getEmail());
        updateUserPassword(user, resetPasswordRequest.getPassword());
        deleteUserTokenById(resetPasswordToken.getId());
        return Map.of("message", "Password reset successfully");
    }

    private void validateToken(String token) {
        if(token == null || token.isEmpty()){
            throw new HttpException("Token is required", HttpStatus.BAD_REQUEST);
        }
    }

    private ResetPasswordToken findResetPasswordToken(String token) {
        ResetPasswordToken resetPasswordToken = passwordRepository.findByToken(token);
        if(resetPasswordToken == null){
            throw new HttpException("Invalid token", HttpStatus.BAD_REQUEST);
        }
        return resetPasswordToken;
    }

    private void validateTokenExpiry(ResetPasswordToken resetPasswordToken) {
        Date currentDate = new Date();
        if(currentDate.after(resetPasswordToken.getExpiryDate())){
            throw new HttpException("Token has expired", HttpStatus.BAD_REQUEST);
        }
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private void updateUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    protected   void deleteUserTokenById(int id) {
        passwordRepository.deleteById(id);
    }
}