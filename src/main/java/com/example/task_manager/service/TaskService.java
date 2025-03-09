package com.example.task_manager.service;

import com.example.task_manager.dto.TaskBoardDTO;
import com.example.task_manager.dto.TaskDTO;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskDTO createTask(Task task, String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        task.setUser(user);
        return new TaskDTO(taskRepository.save(task));
    }

    public List<TaskDTO> getTasksForUser(String username) {
        return taskRepository.findByUserUsername(username).stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList());
    }

    public void deleteTask(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task with task ID " + taskId + " not found"));

        if (task.getStatus() == Task.TaskStatus.DONE) {
            throw new IllegalStateException("Cannot delete completed tasks");
        }
        taskRepository.deleteById(taskId);
    }

    public void updateTask(Task task) {
        taskRepository.findById(task.getId())
                .orElseThrow(() -> new RuntimeException("Task with task ID " + task.getId() + " not found"));
        taskRepository.save(task);
    }

    public void updateStatus(String taskID, Task.TaskStatus status) {
        Task task = taskRepository.findById(taskID)
                .orElseThrow(() -> new RuntimeException("Task with task ID " + taskID + " not found"));

        if (Task.TaskStatus.DONE.equals(status) && (task.getDescription() == null || task.getDescription().isEmpty())) {
            throw new IllegalArgumentException("Task with task ID " + taskID + " has no description");
        }
        if (Task.TaskStatus.DONE.equals(status) && (task.getStatus() == null || Task.TaskStatus.TODO.equals(task.getStatus()))) {
            throw new IllegalArgumentException("You must move to IN_PROGRESS before marking DONE");
        }

        if (task.getStatus() != Task.TaskStatus.DONE && status == Task.TaskStatus.DONE) {
            task.setFinishedAt(LocalDateTime.now());
            task.setDuration(Duration.between(task.getCreatedAt(), LocalDateTime.now()));
        }

        task.setStatus(status);
        taskRepository.save(task);
    }

    public TaskDTO getTaskForUser(String username, String taskId) {
        List<Task> tasks = taskRepository.findByUserUsername(username);
        return tasks.stream()
                .filter(task -> task.getId().equals(taskId))
                .map(TaskDTO::new)
                .findFirst()
                .orElse(null);
    }

    public void updatePriority(String taskID, Task.TaskPriority priority) {
        Task task = taskRepository.findById(taskID)
                .orElseThrow(() -> new RuntimeException("Task with task ID " + taskID + " not found"));
        task.setPriority(priority);
        taskRepository.save(task);
    }

    public List<TaskDTO> getTasksByPriority(String username, Task.TaskPriority priority) {
        return taskRepository.findByUserUsernameAndPriority(username, priority).stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByStatus(String username, Task.TaskStatus status) {
        return taskRepository.findByUserUsernameAndStatus(username, status).stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksSortedByPriority(String username) {
        return taskRepository.findByUserUsernameOrderByPriority(username).stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksSortedByStatus(String username) {
        return taskRepository.findByUserUsernameOrderByStatus(username).stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByPriorityAndStatus(String username, Task.TaskPriority priority, Task.TaskStatus status) {
        return taskRepository.findByUserUsernameAndPriorityAndStatus(username, priority, status).stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByPrioritySortedByStatus(String username, Task.TaskPriority priority) {
        return taskRepository.findByUserUsernameAndPriorityOrderByStatus(username, priority).stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByStatusSortedByPriority(String username, Task.TaskStatus status) {
        return taskRepository.findByUserUsernameAndStatusOrderByPriority(username, status).stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasks(String username, Task.TaskStatus status, Task.TaskPriority priority, String sortBy) {
        if (priority != null && status != null) {
            return getTasksByPriorityAndStatus(username, priority, status);
        } else if (priority != null) {
            if (sortBy != null && sortBy.equals("status")) {
                return getTasksByPrioritySortedByStatus(username, priority);
            } else {
                return getTasksByPriority(username, priority);
            }
        } else if (status != null) {
            if (sortBy != null && sortBy.equals("priority")) {
                return getTasksByStatusSortedByPriority(username, status);
            } else {
                return getTasksByStatus(username, status);
            }
        } else if (sortBy != null) {
            if (sortBy.equals("priority")) {
                return getTasksSortedByPriority(username);
            } else if (sortBy.equals("status")) {
                return getTasksSortedByStatus(username);
            } else {
                return getTasksForUser(username);
            }
        } else {
            return getTasksForUser(username);
        }
    }

    public TaskBoardDTO getGlobalTaskBoardDTO() {
        TaskBoardDTO globalTaskBoardDTO = new TaskBoardDTO();

        globalTaskBoardDTO.setTodo(taskRepository.findByStatus(Task.TaskStatus.TODO)
                .stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList())
        );

        globalTaskBoardDTO.setInProgress(taskRepository.findByStatus(Task.TaskStatus.IN_PROGRESS)
                .stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList())
        );

        globalTaskBoardDTO.setDone(taskRepository.findByStatus(Task.TaskStatus.DONE)
                .stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList())
        );

        return globalTaskBoardDTO;
    }

    public void setDueDate(String taskId, LocalDateTime dueDate) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task with task ID " + taskId + " not found"));

        task.setDueAt(dueDate);
        taskRepository.save(task);
    }
}
