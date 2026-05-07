package com.example.verdandi.service;

import com.example.verdandi.model.Project;
import com.example.verdandi.model.SubProject;
import com.example.verdandi.repository.ProjectRepo;
import com.example.verdandi.repository.SubProjectRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepo projectRepo;
    private final SubProjectRepo subProjectRepo;

    public ProjectService (ProjectRepo projectRepo, SubProjectRepo subProjectRepo){
        this.projectRepo = projectRepo;
        this.subProjectRepo = subProjectRepo;
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
//     public List<Project>getProjects(){
//         try {
//             return projectRepo.getProjects();
//         } catch (DataAccessException ex) {
//             throw new DatabaseOperationException("Failed to retrieve data for project", ex); {
//             }
//         }
    }
}
