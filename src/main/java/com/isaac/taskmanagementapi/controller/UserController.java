package com.isaac.taskmanagementapi.controller;

import com.isaac.taskmanagementapi.dto.password.UpdatePasswordRequest;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.service.password.UpdatePasswordService;
import com.isaac.taskmanagementapi.service.user.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearer-key")
public class UserController {
    private final UserService userService;
    private final UpdatePasswordService updatePasswordService;

    public UserController(UserService userService,
                          UpdatePasswordService updatePasswordService) {
        this.userService = userService;
        this.updatePasswordService = updatePasswordService;
    }
    @GetMapping("/profile")
    public ResponseEntity<Object> getUserProfile() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok().body(userService
                .getUserProfile(user.getUsername()));
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<Object> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok().body(updatePasswordService
                .updateUserPassword(user.getUsername(), request));
    }

}
