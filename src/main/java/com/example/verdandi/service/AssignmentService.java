package com.example.verdandi.service;

import com.example.verdandi.exception.AccessDeniedException;
import com.example.verdandi.repository.AssignmentRepo;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {

    private final AssignmentRepo assignmentRepo;

    public AssignmentService(AssignmentRepo assignmentRepo) {
        this.assignmentRepo = assignmentRepo;
    }

    public void validateUserHasAccessToProject(int projectId, Profile profile) {

        if (!profile.getRole.equals("admin")) {
            if (!assignmentRepo.isUserAssignedToProject(projectId, profile.getId)) {
                throw new AccessDeniedException("You do not have access to this project");
            }
        }

    }
}
