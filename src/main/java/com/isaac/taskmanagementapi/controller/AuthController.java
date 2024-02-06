package com.isaac.taskmanagementapi.controller;

import com.isaac.taskmanagementapi.dto.password.ForgetPasswordRequest;
import com.isaac.taskmanagementapi.dto.auth.SignInRequest;
import com.isaac.taskmanagementapi.dto.auth.SignUpUserRequest;
import com.isaac.taskmanagementapi.dto.password.ResetPasswordRequest;
import com.isaac.taskmanagementapi.service.auth.AuthService;
import com.isaac.taskmanagementapi.service.password.ForgetPasswordService;
import com.isaac.taskmanagementapi.service.password.ResetPasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final ForgetPasswordService forgetPasswordService;
    private final ResetPasswordService resetPasswordService;



    @Autowired
    public AuthController(AuthService authService,
                          ForgetPasswordService forgetPasswordService,
                          ResetPasswordService resetPasswordService) {
        this.authService = authService;
        this.forgetPasswordService = forgetPasswordService;
        this.resetPasswordService = resetPasswordService;
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
        return ResponseEntity.ok().body(forgetPasswordService.forgotPassword(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestParam("token") String token,
                                                @Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok().body(resetPasswordService.resetPassword(token, request));
    }
}
