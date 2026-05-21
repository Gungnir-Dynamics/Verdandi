package com.example.verdandi.service;

import com.example.verdandi.exception.DatabaseOperationException;
import com.example.verdandi.exception.ResourceNotFoundException;
import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Task;
import com.example.verdandi.model.User;
import com.example.verdandi.repository.TaskRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepo taskRepo;
    private final SubProjectService subProjectService;

    public TaskService(TaskRepo taskRepo, SubProjectService subProjectService) {
        this.taskRepo = taskRepo;
        this.subProjectService = subProjectService;
    }

    public void validateTaskBelongsToSubProject(int projectId, int subprojectId, int taskId, User user) {
        subProjectService.validateSubProjectBelongsToProject(projectId, subprojectId, user);

        try {
            if (!taskRepo.taskBelongsToSubproject(subprojectId, taskId)) {
                throw new ResourceNotFoundException(
                        "task " + taskId + " does not belong to subproject " + subprojectId
                );
            }
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Tasks could not be loaded. Please try again later", ex);
        }
    }

    public List<Task> getTasksBySubproject(int projectId, int subprojectId, User user) {
        subProjectService.validateSubProjectBelongsToProject(projectId, subprojectId, user);
        try {
            return taskRepo.getTasks(subprojectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Tasks could not be loaded. Please try again later", ex);
        }
    }

    private void validateTaskData(Task task) {

        if (task.getName() == null || task.getName().isBlank()) {
            throw new ValidationException("The task name cannot be empty");
        }

        if (task.getName().length() > 100) {
            throw new ValidationException("Task name cannot exceed 100 characters.");
        }

        if (task.getDescription() != null && task.getDescription().length() > 1500) {
            throw new ValidationException("Description cannot exceed 1500 characters.");
        }

        if (task.getEstimatedHours() < 0) {
            throw new ValidationException("Estimated hours must be 0 or higher.");
        }
    }

    public void createTask(int projectId, int subprojectId, Task task, User user) {
        subProjectService.validateSubProjectBelongsToProject(projectId, subprojectId, user);
        validateTaskData(task);

        try {
            taskRepo.createTask(subprojectId, task);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to create task.", ex);
        }
    }

    public Task getSingleTask(int projectId, int subprojectId, int taskId, User user) {
        validateTaskBelongsToSubProject(projectId, subprojectId, taskId, user);

        try {
            return taskRepo.getSingleTask(taskId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to retrieve task.", ex);
        }
    }

    public void updateTask(int projectId, int subprojectId, int taskId, Task updatedTask, User user) {
        validateTaskBelongsToSubProject(projectId, subprojectId, taskId, user);
        validateTaskData(updatedTask);

        try {
            taskRepo.updateTask(taskId, updatedTask);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to update task.", ex);
        }
    }

    public void deleteTask(int projectId, int subprojectId, int taskId, User user) {
        validateTaskBelongsToSubProject(projectId, subprojectId, taskId, user);

        try {
            taskRepo.deleteTask(taskId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to delete task.", ex);
        }
    }
}
