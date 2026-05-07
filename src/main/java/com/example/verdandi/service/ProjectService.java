package com.example.verdandi.service;

import com.example.verdandi.exception.DatabaseOperationException;
import com.example.verdandi.model.Project;
import com.example.verdandi.repository.ProjectRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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
        return projectRepo.getSingleProject(projectId);
    }

    public void saveProject(Project project) {
        projectRepo.createProject(project);
    }
}
