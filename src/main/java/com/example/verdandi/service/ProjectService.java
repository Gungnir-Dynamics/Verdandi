package com.example.verdandi.service;

import com.example.verdandi.model.Project;
import com.example.verdandi.model.SubProject;
import com.example.verdandi.repository.ProjectRepo;
import com.example.verdandi.repository.SubProjectRepo;
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

    public List<Project>getProjects(){
        return projectRepo.getProjects();
    }
}