package com.example.verdandi.service;

import com.example.verdandi.exception.DatabaseOperationException;
import com.example.verdandi.exception.ResourceNotFoundException;
import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Project;
import com.example.verdandi.repository.ProjectRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepo projectRepo;

    public ProjectService(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    private void validateProject(Project project) {

        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new ValidationException("Project name is required");
        }

        if (project.getName().trim().length() < 3) {
            throw new ValidationException("Project name need to contain a minimum of 3 characters");
        }

        if (project.getName().trim().length() > 100) {
            throw new ValidationException("Project name can not contain more then 100 characters ");
        }


        if (project.getDeadline() != null && project.getDeadline().isBefore(LocalDate.now())) {
            throw new ValidationException("Deadline can not be before today's date");
        }


        if (project.getDescription() != null && project.getDescription().length() > 5000) {
            throw new ValidationException("Description must be a maximum of 5000 characters");
        }
    }

    public void validateProjectExists(int projectId) {
        try {
            if (!projectRepo.projectExists(projectId)) {
                throw new ResourceNotFoundException(
                        "project " + projectId + " does not exist"
                );
            }
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to retrieve data for project", ex);
        }
    }


    public List<Project> getMultipleProjects() {
        try {
            return projectRepo.getMultipleProjects();
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to retrieve data for project", ex);
        }
    }

    public Project getSingleProject(int projectId) {
        try {
            return projectRepo.getSingleProject(projectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to retrieve data for project", ex);
        }

    }

    public void saveProject(Project project) {
        validateProject(project);

        try {
            projectRepo.createProject(project);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to create project", ex);
        }
    }

    public void updateProject(int projectId, Project updateProject) {
        validateProject(updateProject);
        validateProjectExists(projectId);

        try {
            projectRepo.updateProject(projectId, updateProject);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to update project", ex);
        }

    }

    public void deleteProject(int projectId) {
        validateProjectExists(projectId);
        try {
            projectRepo.deleteProject(projectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to delete project", ex);
        }
    }
}
