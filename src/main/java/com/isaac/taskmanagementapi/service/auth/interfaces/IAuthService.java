package com.isaac.taskmanagementapi.service.auth.interfaces;

import com.isaac.taskmanagementapi.dto.auth.SignInRequest;
import com.isaac.taskmanagementapi.dto.auth.SignUpUserRequest;

public interface IAuthService {
    Object signUp(SignUpUserRequest user);
    Object signIn(SignInRequest user);
}