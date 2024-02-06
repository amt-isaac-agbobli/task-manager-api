package com.isaac.taskmanagementapi.repository;

import com.isaac.taskmanagementapi.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<ResetPasswordToken, Integer> {
    public ResetPasswordToken findByEmail(String email);

    public ResetPasswordToken findByToken(String token);

    public void deleteByToken(String token);
}
