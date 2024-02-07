package com.isaac.taskmanagementapi.entity;

import com.isaac.taskmanagementapi.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = LocalDate.now();
    }

}