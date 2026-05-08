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

    public void validateProjectDeadlineNotBeforeToday(Project project){
            if (project.getDeadline() != null && project.getDeadline().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Deadline må ikke være før dagens dato");
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
        projectRepo.createProject(project);
    }
}
