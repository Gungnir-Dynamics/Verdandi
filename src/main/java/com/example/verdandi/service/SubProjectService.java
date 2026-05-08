package com.example.verdandi.service;


import com.example.verdandi.exception.DatabaseOperationException;
import com.example.verdandi.exception.ResourceNotFoundException;
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
            throw new DatabaseOperationException("Failed to retrieve subprojects", ex);
        }

    }
    public void saveSubProject(SubProject subProject) {
        validateSubProjectBelongsToProject(subProject.getProjectId(), subProject.getId());
        try {
            subProjectRepo.createSubProject(subProject);

        } catch (DataAccessException ex) {

            throw new DatabaseOperationException("Failed to create subproject", ex);
        }
    }
}

