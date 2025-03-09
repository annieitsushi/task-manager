package com.example.task_manager.controller;

import com.example.task_manager.dto.TaskBoardDTO;
import com.example.task_manager.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
public class TaskBoardController {

    private final TaskService taskService;

    public TaskBoardController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public TaskBoardDTO getTaskBoardDTO() {
        return taskService.getGlobalTaskBoardDTO();
    }
}
