package com.example.task_manager;

import com.example.task_manager.dto.TaskDTO;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    public TaskRepository taskRepository;
    @Mock
    public UserRepository userRepository;
    @InjectMocks
    private TaskService taskService;

    @Test
    public void testCreateTask() {
        User mockUser = new User("annie", "1234", "Annie", "Nyagolova", "annie@gmail.com");
        Mockito.when(userRepository.findById("annie")).thenReturn(Optional.of(mockUser));

        Task task = new Task("Test Task", "This is a test task", Task.TaskPriority.LOW, mockUser);
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        Mockito.when(taskRepository.save(captor.capture())).thenReturn(task);

        TaskDTO resultTask = taskService.createTask(task, "annie");

        assertNotNull(resultTask);
        assertEquals("Test Task", resultTask.getTitle());
        assertEquals("This is a test task", resultTask.getDescription());
        Task argument = captor.getValue();
        assertThat(argument.getUser(), is(mockUser));

        Mockito.verify(taskRepository, Mockito.times(1)).save(any(Task.class));
    }

    @Test
    public void testCreateTaskNoUser() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            Task task = new Task("Test Task", "This is a test task", Task.TaskPriority.CRITICAL, null);
            taskService.createTask(task, "annie");
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertNotNull(exception.getReason(), "Exception reason should not be null");
        assertTrue(exception.getReason().contains("User not found"));
    }

    @Test
    public void testGetTaskWithSorting() {
        User mockUser = new User("annie", "1234", "Annie", "Nyagolova", "annie@gmail.com");

        Task task1 = new Task("Task 1", "Desc 1", Task.TaskPriority.LOW, mockUser);
        Task task2 = new Task("Task 2", "Desc 2", Task.TaskPriority.HIGH, mockUser);
        Task task3 = new Task("Task 3", "Desc 3", Task.TaskPriority.CRITICAL, mockUser);
        Task task4 = new Task("Task 4", "Desc 4", Task.TaskPriority.MEDIUM, mockUser);
        task1.setStatus(Task.TaskStatus.TODO);
        task2.setStatus(Task.TaskStatus.IN_PROGRESS);
        task3.setStatus(Task.TaskStatus.DONE);
        task4.setStatus(Task.TaskStatus.TODO);

        Mockito.when(taskRepository.findByUserUsernameAndStatusOrderByPriority("annie", Task.TaskStatus.TODO))
                .thenReturn(List.of(task4, task1));

        List<TaskDTO> tasks = taskService.getTasksByStatusSortedByPriority("annie", Task.TaskStatus.TODO);

        assertEquals(2, tasks.size());
        Mockito.verify(taskRepository, Mockito.times(1)).findByUserUsernameAndStatusOrderByPriority(any(String.class), any(Task.TaskStatus.class));
    }

    @Test
    public void testGetTasksSortedByStatus() {
        User mockUser = new User("annie", "1234", "Annie", "Nyagolova", "annie@gmail.com");

        Task task1 = new Task("Task 1", "Desc 1", Task.TaskPriority.LOW, mockUser);
        Task task2 = new Task("Task 2", "Desc 2", Task.TaskPriority.HIGH, mockUser);
        Task task3 = new Task("Task 3", "Desc 3", Task.TaskPriority.CRITICAL, mockUser);
        Task task4 = new Task("Task 4", "Desc 4", Task.TaskPriority.MEDIUM, mockUser);
        task1.setStatus(Task.TaskStatus.IN_PROGRESS);
        task2.setStatus(Task.TaskStatus.TODO);
        task3.setStatus(Task.TaskStatus.DONE);
        task4.setStatus(Task.TaskStatus.IN_PROGRESS);

        Mockito.when(taskRepository.findByUserUsernameOrderByStatus("annie"))
                .thenReturn(List.of(task2, task1, task4, task3)); // Expect sorted by status

        List<TaskDTO> tasks = taskService.getTasks("annie", null, null, "status");

        assertEquals(4, tasks.size());
        assertEquals(Task.TaskStatus.TODO, tasks.get(0).getStatus());
        assertEquals(Task.TaskStatus.DONE, tasks.get(3).getStatus());

        Mockito.verify(taskRepository, Mockito.times(1)).findByUserUsernameOrderByStatus(any(String.class));
    }
}