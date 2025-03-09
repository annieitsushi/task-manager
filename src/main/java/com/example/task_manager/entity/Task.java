package com.example.task_manager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name="TASKS", schema = "TASKMANAGER")
@EqualsAndHashCode
public class Task {
    @Id
    private String id;
    @Setter
    private String title;
    @Setter
    private String description;
    @ManyToOne(optional = false)
    @Setter
    private User user;

    public enum TaskStatus {
        TODO,
        IN_PROGRESS,
        DONE
    }
    @Enumerated(EnumType.STRING)
    @Setter
    private TaskStatus status;

    @Getter
    public enum TaskPriority {
        LOW(1),
        MEDIUM(2),
        HIGH(3),
        CRITICAL(4);

        private final int weight;

        TaskPriority(int weight) {
            this.weight = weight;
        }
    }
    @Enumerated(EnumType.STRING)
    @Setter
    private TaskPriority priority;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Setter
    private LocalDateTime dueAt;
    @Setter
    private LocalDateTime finishedAt;
    @Setter
    private Duration duration;

    public Task() {
        this.id = UUID.randomUUID().toString();
        this.status = TaskStatus.TODO;
        this.priority = TaskPriority.LOW;
    }

    public Task(String title, String description, TaskPriority priority, User user) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.user = user;
        this.status = TaskStatus.TODO;
        this.priority = priority;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.dueAt = LocalDate.now().plusDays(7).atStartOfDay();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @AssertTrue(message = "All tasks should have a valid title and priority.")
    public boolean validate() {
        return !(this.getTitle() == null || this.getTitle().isEmpty() || this.getPriority() == null);
    }

}
