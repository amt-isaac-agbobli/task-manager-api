package com.isaac.taskmanagementapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public class Exception {
    private final String message;
    private  final  Throwable throwable;
    private final HttpStatus httpStatus;
}
