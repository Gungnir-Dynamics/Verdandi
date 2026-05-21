package com.example.verdandi.service;


import com.example.verdandi.exception.DatabaseOperationException;
import com.example.verdandi.exception.ResourceNotFoundException;
import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.SubProject;
import com.example.verdandi.model.User;
import com.example.verdandi.repository.SubProjectRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubProjectService {
    private final SubProjectRepo subProjectRepo;
    private final ProjectService projectService;

    public SubProjectService(SubProjectRepo subProjectRepo, ProjectService projectService) {
        this.subProjectRepo = subProjectRepo;
        this.projectService = projectService;
    }

    public List<SubProject> getSubProjects(int projectId, User user) {
        projectService.validateProjectAccess(projectId, user);
        try {

            return subProjectRepo.getSubProjects(projectId);

        } catch (DataAccessException ex) {

            throw new DatabaseOperationException("Failed to retrieve subprojects", ex);
        }
    }

    public void validateSubProjectBelongsToProject(int projectId, int subprojectId, User user) {
        projectService.validateProjectAccess(projectId, user);
        try {
            if (!subProjectRepo.subprojectBelongsToProject(projectId, subprojectId)) {
                throw new ResourceNotFoundException("Subproject " + subprojectId + " does not belong to project " + projectId);
            }
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("The system could not verify that this subproject belongs to the selected project", ex);
        }
    }

    public void validateSubProjectData(SubProject subProject) {
        if (subProject.getName() == null || subProject.getName().isBlank()) {
            throw new ValidationException("The subproject name cannot be empty");
        }

        if (subProject.getName().length() > 100) {
            throw new ValidationException("Subproject name cannot exceed 100 characters.");
        }

        if (subProject.getDescription() != null && subProject.getDescription().length() > 1500) {
            throw new ValidationException("The description may not exceed 1500 characters");
        }

        if (subProject.getEstimatedHours() < 0) {
            throw new ValidationException("Estimated hours must be 0 or higher.");
        }
    }


    public void saveSubProject(SubProject subProject, int ProjectId, User user) {

        projectService.validateProjectAccess(subProject.getProjectId(), user);
        validateSubProjectData(subProject);

        try {
            subProjectRepo.createSubProject(subProject, ProjectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to create subproject", ex);
        }
    }


    public void updateSubProject(int projectId,
                                 int subprojectId,
                                 SubProject updatedSubproject,
                                 User user) {

        validateSubProjectBelongsToProject(projectId, subprojectId, user);
        validateSubProjectData(updatedSubproject);

        try {
            subProjectRepo.updateSubProject(subprojectId, updatedSubproject);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to update subproject", ex);
        }
    }


    public void deleteSubproject(int projectId, int subprojectId, User user) {
        validateSubProjectBelongsToProject(projectId, subprojectId, user);

        try {

            subProjectRepo.deleteSubProject(subprojectId);

        } catch (DataAccessException ex) {

            throw new DatabaseOperationException("Failed to delete subproject", ex);
        }
    }

    public SubProject findSubProjectById(int projectId, int subprojectId, User user) {

        validateSubProjectBelongsToProject(projectId, subprojectId, user);

        try {
            SubProject subProject = subProjectRepo.findSubProjectById(subprojectId);

            if (subProject == null) {

                throw new ResourceNotFoundException("Subproject with id: " + subprojectId + " not found");
            }

            return subProject;

        } catch (DataAccessException ex) {

            throw new DatabaseOperationException("Failed to retrieve subproject", ex);
        }
    }
}