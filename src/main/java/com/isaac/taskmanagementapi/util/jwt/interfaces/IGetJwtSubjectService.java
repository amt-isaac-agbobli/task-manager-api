package com.isaac.taskmanagementapi.util.jwt.interfaces;

public interface IGetJwtSubjectService {
    String execute(String token);
}