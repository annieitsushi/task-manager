package com.example.task_manager.dto;

import com.example.task_manager.entity.Task;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TaskDTO {
    private String id;
    private String title;
    private String description;
    private String assignedTo;
    private Task.TaskStatus status;
    private Task.TaskPriority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.assignedTo = (task.getUser() != null) ? task.getUser().getUsername() : null;
        this.status = task.getStatus();
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
        this.priority = task.getPriority();
        this.dueDate = task.getDueAt();
    }
}
