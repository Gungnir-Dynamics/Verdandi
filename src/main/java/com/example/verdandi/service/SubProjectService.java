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

    public SubProjectService(SubProjectRepo subProjectRepo) {
        this.subProjectRepo = subProjectRepo;
    }

    public List<SubProject> getSubProjects(int projectId) {
        try {
            return subProjectRepo.getSubProjects(projectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to retrieve subprojects", ex);
        }
    }

    public void validateSubprojectBelongsToProject(int projectId, int subprojectId) {
        if (!subProjectRepo.subprojectBelongsToProject(projectId, subprojectId)) {
            throw new ResourceNotFoundException(
                    "Subproject " + subprojectId + " does not belong to project " + projectId
            );
        }
    }
}

