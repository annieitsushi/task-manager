package com.example.task_manager;

import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindTask() {
        User user = new User("annie", "hashedPassword", "Annie", "Nyagolova", "annie@gmail.com");
        userRepository.save(user);

        Task task = new Task("Test Task", "This is a test task", Task.TaskPriority.HIGH, user);
        taskRepository.save(task);

        Optional<Task> foundTask = taskRepository.findById(task.getId());
        assertTrue(foundTask.isPresent());
        assertEquals("Test Task", foundTask.get().getTitle());
        assertEquals(Task.TaskPriority.HIGH, foundTask.get().getPriority());
    }

    @Test
    public void testFindByUserUsername() {
        User user = new User("annie", "hashedPassword", "Annie", "Nyagolova", "annie@gmail.com");
        userRepository.save(user);

        Task task1 = new Task("Task 1", "Description 1", Task.TaskPriority.LOW, user);
        Task task2 = new Task("Task 2", "Description 2", Task.TaskPriority.CRITICAL, user);
        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> tasks = taskRepository.findByUserUsername("annie");

        assertEquals(2, tasks.size());
        assertThat(tasks.stream().map(Task::getTitle).toList(), containsInAnyOrder("Task 1", "Task 2"));
    }

    @Test
    public void testFindByStatus() {
        User user = new User("annie", "hashedPassword", "Annie", "Nyagolova", "annie@gmail.com");
        userRepository.save(user);

        Task task1 = new Task("Task 1", "Desc 1", Task.TaskPriority.LOW, user);
        Task task2 = new Task("Task 2", "Desc 2", Task.TaskPriority.HIGH, user);
        task1.setStatus(Task.TaskStatus.TODO);
        task2.setStatus(Task.TaskStatus.DONE);
        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> todoTasks = taskRepository.findByUserUsernameAndStatus("annie",Task.TaskStatus.TODO);
        List<Task> doneTasks = taskRepository.findByUserUsernameAndStatus("annie",Task.TaskStatus.DONE);

        assertEquals(1, todoTasks.size());
        assertThat(todoTasks, contains(hasProperty("title", is("Task 1"))));

        assertEquals(1, doneTasks.size());
        assertThat(doneTasks, contains(hasProperty("title", is("Task 2"))));
    }
}
