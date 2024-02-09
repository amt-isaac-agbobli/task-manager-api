package com.isaac.taskmanagementapi.controller.user;

import com.isaac.taskmanagementapi.dto.password.UpdatePasswordRequest;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.service.password.UpdatePasswordService;
import com.isaac.taskmanagementapi.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
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
    @Operation(description = "Get user profile")
    public ResponseEntity<Object> getUserProfile() {

        User user = authenticatedUser();

        return ResponseEntity.ok().body(userService
                .getUserProfile(user.getUsername()));
    }

    @PutMapping("/updatePassword")
    @Operation(description = "Update user password")
    public ResponseEntity<Object> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {

        User user = authenticatedUser();

        return ResponseEntity.ok().body(updatePasswordService
                .updateUserPassword(user.getUsername(), request));
    }

    @PatchMapping("/updateProfile")
    @Operation(description = "Update user profile")
    public ResponseEntity<Object> updateProfile(@Valid @RequestParam("name") String name) {

        User user = authenticatedUser();

        return ResponseEntity.ok().body(userService
                .updateUserProfile(user.getUsername(), name));
    }

    @GetMapping("/all")
    @Operation(description = "Get all users")
    public ResponseEntity<Object> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok().body(userService.getAllUsers(pageable));
    }

    @GetMapping("/friend")
    @Operation(description = "Get a user by email")
    public ResponseEntity<Object> getFriendByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok().body(userService.getFriendByEmail(email));
    }

    private User authenticatedUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        return (User) authentication.getPrincipal();
    }

}