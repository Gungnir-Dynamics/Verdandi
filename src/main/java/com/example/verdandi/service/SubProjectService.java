package com.example.verdandi.service;


import com.example.verdandi.model.SubProject;
import com.example.verdandi.repository.SubProjectRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubProjectService {
    private final SubProjectRepo subProjectRepo;

    public SubProjectService(SubProjectRepo subProjectRepo) {
        this.subProjectRepo = subProjectRepo;
    }

    public List<SubProject> getSubProjects(int projectId) {
//        try {
        return subProjectRepo.getSubProjects(projectId);
//        } catch (DataAccessException ex) {
//            throw new DataOperationException("Failed to retrieve subprojects", ex);
        }
    }

}