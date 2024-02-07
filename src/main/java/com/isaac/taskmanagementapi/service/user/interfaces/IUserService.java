package com.isaac.taskmanagementapi.service.user.interfaces;

import com.isaac.taskmanagementapi.dto.password.UpdatePasswordRequest;
import com.isaac.taskmanagementapi.entity.User;

public interface IUserService {
    User getUserByEmail(String email);
    User getUserProfile(String email);
}