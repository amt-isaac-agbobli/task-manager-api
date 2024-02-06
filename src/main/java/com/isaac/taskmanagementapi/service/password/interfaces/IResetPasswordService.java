package com.isaac.taskmanagementapi.service.password.interfaces;

import com.isaac.taskmanagementapi.dto.password.ResetPasswordRequest;

public interface IResetPasswordService {
    Object resetPassword(String token, ResetPasswordRequest resetPasswordRequest);
}