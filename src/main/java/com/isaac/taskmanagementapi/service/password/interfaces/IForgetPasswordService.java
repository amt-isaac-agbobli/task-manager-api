package com.isaac.taskmanagementapi.service.password.interfaces;

import com.isaac.taskmanagementapi.dto.password.ForgetPasswordRequest;

public interface IForgetPasswordService {
    Object forgotPassword(ForgetPasswordRequest forgetPasswordRequest);
}