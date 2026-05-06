package com.example.verdandi.service;

import com.example.verdandi.model.Task;
import com.example.verdandi.repository.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo){
        this.taskRepo = taskRepo;
    }

    public List<Task> getTasksBySubproject(int subprojectId){
        return taskRepo.getTasks(subprojectId);
    }
}
