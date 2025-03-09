package com.example.task_manager.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TaskBoardDTO {
    private List<TaskDTO> todo = new ArrayList<>();
    private List<TaskDTO> inProgress = new ArrayList<>();
    private List<TaskDTO> done = new ArrayList<>();
}
