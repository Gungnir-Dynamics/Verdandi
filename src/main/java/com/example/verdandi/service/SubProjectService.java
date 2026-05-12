package com.example.verdandi.service;


import com.example.verdandi.exception.DatabaseOperationException;
import com.example.verdandi.exception.ResourceNotFoundException;
import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.SubProject;
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

    public List<SubProject> getSubProjects(int projectId) {
        projectService.validateProjectExists(projectId);
        try {

            return subProjectRepo.getSubProjects(projectId);

        } catch (DataAccessException ex) {

            throw new DatabaseOperationException("Failed to retrieve subprojects", ex);
        }
    }


    public void validateSubProjectBelongsToProject(int projectId, int subprojectId) {
        projectService.validateProjectExists(projectId);
        try {
            if (!subProjectRepo.subprojectBelongsToProject(projectId, subprojectId)) {

                throw new ResourceNotFoundException(

                        "Subproject " + subprojectId + " does not belong to project " + projectId
                );
            }
        } catch (DataAccessException ex) {

            throw new DatabaseOperationException("Failed to validate subproject ownership", ex);
        }
    }


    public void saveSubProject(SubProject subProject, int ProjectId) {

        projectService.validateProjectExists(subProject.getProjectId());

        try {
            subProjectRepo.createSubProject(subProject, ProjectId);

        } catch (DataAccessException ex) {

            throw new DatabaseOperationException("Failed to create subproject", ex);
        }
    }


    public void updateSubProject(SubProject subProject) {

        projectService.validateProjectExists(subProject.getProjectId());

        validateSubProjectBelongsToProject(subProject.getProjectId(), subProject.getId());

        try {

            subProjectRepo.updateSubProject(subProject);

        } catch (DataAccessException ex) {

            throw new DatabaseOperationException("Failed to update subproject", ex);
        }
    }


    public void deleteSubproject(int projectId, int subprojectId) {
        validateSubProjectBelongsToProject(projectId, subprojectId);

        try {

            subProjectRepo.deleteSubProject(subprojectId);

        } catch (DataAccessException ex) {

            throw new DatabaseOperationException("Failed to delete subproject", ex);
        }
    }

    public SubProject findSubProjectById(int id) {

        try {
            SubProject subProject = subProjectRepo.findSubProjectById(id);

            if (subProject == null) {

                throw new ResourceNotFoundException("Subproject with id: " + id + " not found");
            }

            return subProject;

        } catch (DataAccessException ex) {

            throw new DatabaseOperationException("Failed to retrieve subproject", ex);
        }
    }

    public void validateSubProjectData(SubProject subProject) {
        if (subProject.getName().isBlank() || subProject.getName() == null) {
            throw new ValidationException("Subproject name cannot be empty.");
        }

        if (subProject.getName().length() > 100) {
            throw new ValidationException("Subproject name cannot exceed 100 characters.");
        }

        if (subProject.getDescription() != null && subProject.getDescription().length() > 1500) {
            throw new ValidationException("Description cannot exceed 1500 characters.");
        }

        if (subProject.getEstimatedHours() < 0) {
            throw new ValidationException("Estimated hours must be 0 or higher.");
        }
    }
}