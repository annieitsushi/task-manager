package com.example.task_manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="USERS", schema = "TASKMANAGER")
public class User {
    @Id
    @NotBlank(message = "Username can't be empty")
    private String username;
    @Pattern(regexp = ".{8,}", message = "Password must be at least 8 characters long")
    private String password;
    private String firstName;
    private String lastName;
    @Pattern(regexp = "[A-z0-9]+@[a-z]+\\.[a-z]+", message = "Not correct e-mail format")
    private String email;
}
