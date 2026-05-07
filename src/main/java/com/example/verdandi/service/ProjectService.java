package com.example.verdandi.service;

import com.example.verdandi.model.Project;
import com.example.verdandi.repository.ProjectRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepo projectRepo;

    public ProjectService (ProjectRepo projectRepo){
        this.projectRepo = projectRepo;
    }

    public List<Project>getProjects(){
        try {
            return projectRepo.getProjects();
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to retrieve data for project", ex); {
            }
        }
    }

}
