package com.isaac.taskmanagementapi.repository;

import com.isaac.taskmanagementapi.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<ResetPasswordToken, Integer> {
    public ResetPasswordToken findByEmail(String email);

    ResetPasswordToken findByToken(String token);
}
