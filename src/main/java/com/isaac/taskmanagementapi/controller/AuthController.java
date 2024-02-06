package com.isaac.taskmanagementapi.controller;

import com.isaac.taskmanagementapi.dto.SignUpUserRequest;
import com.isaac.taskmanagementapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@Valid @RequestBody SignUpUserRequest user ) {
        return ResponseEntity.status(201).body(authService.signUp(user));
    }
}
