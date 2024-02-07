package com.isaac.taskmanagementapi.service.password.interfaces;

import com.isaac.taskmanagementapi.dto.password.UpdatePasswordRequest;

public interface IUpdatePasswordService {
    Object updateUserPassword(String email, UpdatePasswordRequest request);
}
