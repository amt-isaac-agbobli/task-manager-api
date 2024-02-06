package com.isaac.taskmanagementapi.controller;

import com.isaac.taskmanagementapi.dto.password.ForgetPasswordRequest;
import com.isaac.taskmanagementapi.dto.auth.SignInRequest;
import com.isaac.taskmanagementapi.dto.auth.SignUpUserRequest;
import com.isaac.taskmanagementapi.service.auth.AuthService;
import com.isaac.taskmanagementapi.service.password.ForgetPasswordPasswordService;
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
    private final ForgetPasswordPasswordService forgetPasswordPasswordService;

    @Autowired
    public AuthController(AuthService authService,
                          ForgetPasswordPasswordService forgetPasswordPasswordService) {
        this.authService = authService;
        this.forgetPasswordPasswordService = forgetPasswordPasswordService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(@Valid @RequestBody SignUpUserRequest user ) {
        return ResponseEntity.status(201).body(authService.signUp(user));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Object> signIn(@Valid @RequestBody SignInRequest user) {
        return ResponseEntity.ok().body(authService.signIn(user));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@Valid @RequestBody ForgetPasswordRequest request) {
        return ResponseEntity.ok().body(forgetPasswordPasswordService.forgotPassword(request));
    }
}
