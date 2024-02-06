package com.isaac.taskmanagementapi.util.jwt.interfaces;


import com.isaac.taskmanagementapi.entity.User;

public interface ICreateJwtService {
    String execute(User user);
}