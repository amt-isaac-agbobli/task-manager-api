package com.isaac.taskmanagementapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "reset_password_token")
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column( nullable = false, unique = true)
    private String token;

    @Column( nullable = false, unique = true)
    private String email;

    @Column( nullable = false)
    private Date expiryDate;
}
