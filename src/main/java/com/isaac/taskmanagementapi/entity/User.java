package com.isaac.taskmanagementapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    int id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    String email;

    @NotBlank(message = "Password is required")
    @Length(min = 6, message = "Password must be at least 6 characters")
    String password;
}
