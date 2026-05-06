package com.example.verdandi.service;

import com.example.verdandi.exception.DatabaseOperationException;
import com.example.verdandi.model.Task;
import com.example.verdandi.repository.TaskRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo){
        this.taskRepo = taskRepo;
    }

    public List<Task> getTasksBySubproject(int subprojectId){
        try {
            return taskRepo.getTasks(subprojectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to retrieve tasks.", ex);
        }
    }
}
