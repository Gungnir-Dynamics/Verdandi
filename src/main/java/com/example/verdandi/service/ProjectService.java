package com.example.verdandi.service;

import com.example.verdandi.exception.DatabaseOperationException;
import com.example.verdandi.exception.ResourceNotFoundException;
import com.example.verdandi.model.Project;
import com.example.verdandi.repository.ProjectRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepo projectRepo;

    public ProjectService(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
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
        projectRepo.createProject(project);
    }

    public void deleteProject(int projectId){
        projectRepo.deleteProject(projectId);
    }

    private void validateProject(Project project) {

        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Project name is required");
        }

        if (project.getName().trim().length() < 3) {
            throw new IllegalArgumentException("Project name need to contain a minimum of 3 characters");
        }

        if (project.getName().trim().length() > 100) {
            throw new IllegalArgumentException("Project name can not contain more then 100 characters ");
        }


        if (project.getDeadline() != null && project.getDeadline().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline must not be before today's date");
        }


        if (project.getDescription() != null && project.getDescription().length() > 5000) {
            throw new IllegalArgumentException("Description must be a maximum of 5000 characters");
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

}
