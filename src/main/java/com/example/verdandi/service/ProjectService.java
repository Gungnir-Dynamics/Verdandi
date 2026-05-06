package com.example.verdandi.service;

import com.example.verdandi.model.Project;
import com.example.verdandi.repository.ProjectRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepo projectRepo;

    public ProjectService (ProjectRepo projectRepo){
        this.projectRepo = projectRepo;
    }

    public List<Project> getMultipleProjects(){
        return projectRepo.getMultipleProjects();
    }

    public Project getSingleProject(int projectId){
        return projectRepo.getSingleProject(projectId);
    }

    public void saveProject(Project project){
        projectRepo.createProject(project);
    }
}
