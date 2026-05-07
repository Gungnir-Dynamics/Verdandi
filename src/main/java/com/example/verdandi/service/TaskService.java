package com.example.verdandi.service;

import com.example.verdandi.exception.DatabaseOperationException;
import com.example.verdandi.exception.ResourceNotFoundException;
import com.example.verdandi.model.Task;
import com.example.verdandi.repository.TaskRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepo taskRepo;
    private final SubProjectService subProjectService;

    public TaskService(TaskRepo taskRepo, SubProjectService subProjectService){
        this.taskRepo = taskRepo;
        this.subProjectService = subProjectService;
    }

    public List<Task> getTasksBySubproject(int projectId, int subprojectId){
        try {
            subProjectService.validateSubprojectBelongsToProject(projectId, subprojectId);
            return taskRepo.getTasks(subprojectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to retrieve tasks.", ex);
        }
    }

    private void validateProjectHierarchy(int projectId, int subprojectId, int taskId) {
        subProjectService.validateSubprojectBelongsToProject(projectId,subprojectId);

        if (!taskRepo.taskBelongsToSubproject(subprojectId, taskId)) {
            throw new ResourceNotFoundException(
                    "task " + taskId + " does not belong to project " + subprojectId
            );
        }
    }

}
