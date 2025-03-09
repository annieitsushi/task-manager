package com.example.task_manager.controller;

import com.example.task_manager.dto.TaskDTO;
import com.example.task_manager.entity.Task;
import com.example.task_manager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/users/{username}/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskDTO createTask(@PathVariable String username, @RequestBody @Valid Task task) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();
        if (!loggedUsername.equals(username)) {
            throw new AccessDeniedException("You're not allowed to access this user's data.");
        }
        return taskService.createTask(task, username);
    }

    @GetMapping
    public List<TaskDTO> getTasks(@PathVariable String username,
                                  @RequestParam(required = false) Task.TaskPriority priority,
                                  @RequestParam(required = false) Task.TaskStatus status,
                                  @RequestParam(required = false) String sortBy) throws AccessDeniedException {
        return taskService.getTasks(username, status, priority, sortBy);
    }

    @GetMapping("/{taskId}")
    public TaskDTO getTask(@PathVariable String username, @PathVariable String taskId) throws AccessDeniedException {
        return taskService.getTaskForUser(username, taskId);

    }

    @DeleteMapping("/{taskID}")
    public void deleteTask(@PathVariable String username, @PathVariable String taskID) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();
        if (!loggedUsername.equals(username)) {
            throw new AccessDeniedException("You're not allowed to access this user's data.");
        }
        taskService.deleteTask(taskID);
    }

    @PutMapping("/{taskID}")
    public void updateTask(@PathVariable String username, @RequestBody @Valid Task task) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();
        if (!loggedUsername.equals(username)) {
            throw new AccessDeniedException("You're not allowed to access this user's data.");
        }
        taskService.updateTask(task);
    }

    @PutMapping("/{taskID}/status")
    public void updateTaskStatus(@PathVariable String username,
                                 @PathVariable String taskID,
                                 @RequestParam Task.TaskStatus status) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();
        if (!loggedUsername.equals(username)) {
            throw new AccessDeniedException("You're not allowed to access this user's data.");
        }
        taskService.updateStatus(taskID, status);
    }

    @PutMapping("/{taskID}/priority")
    public void updateTaskPriority(@PathVariable String username,
                                   @PathVariable String taskID,
                                   @RequestParam Task.TaskPriority priority) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();
        if (!loggedUsername.equals(username)) {
            throw new AccessDeniedException("You're not allowed to access this user's data.");
        }
        taskService.updatePriority(taskID, priority);
    }
}
