package com.example.task_manager.repository;

import com.example.task_manager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByUserUsername(String username);

    List<Task> findByUserUsernameAndStatus(String username, Task.TaskStatus status);

    List<Task> findByUserUsernameOrderByStatus(String username);

    List<Task> findByUserUsernameAndPriority(String username, Task.TaskPriority priority);

    List<Task> findByUserUsernameOrderByPriority(String username);

    List<Task> findByUserUsernameAndPriorityAndStatus(String username, Task.TaskPriority priority, Task.TaskStatus status);

    List<Task> findByUserUsernameAndPriorityOrderByStatus(String username, Task.TaskPriority priority);

    List<Task> findByUserUsernameAndStatusOrderByPriority(String username, Task.TaskStatus status);

    List<Task> findByStatus(Task.TaskStatus status);
}
