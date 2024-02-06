package com.isaac.taskmanagementapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private int id;

    @Column( nullable = false, unique = true)
    private String token;

    @Column( nullable = false, unique = true)
    private String email;

    @Column( nullable = false)
    private Date expiryDate;
}
