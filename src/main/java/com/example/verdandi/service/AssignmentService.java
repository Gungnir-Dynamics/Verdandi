package com.example.verdandi.service;

import com.example.verdandi.exception.DatabaseOperationException;
import com.example.verdandi.exception.ResourceNotFoundException;
import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.User;
import com.example.verdandi.repository.AssignmentRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {

    private final AssignmentRepo assignmentRepo;
    private final ProjectService projectService;
    private final UserService userService;

    public AssignmentService(AssignmentRepo assignmentRepo,
                             ProjectService projectService,
                             UserService userService) {
        this.assignmentRepo = assignmentRepo;
        this.projectService = projectService;
        this.userService = userService;
    }

    public void assignUserToProject(String email, int projectId, User user) {

        projectService.validateProjectAccess(projectId, user);
        User userToAdd;

        try {
            userToAdd = userService.findUserByEmail(email);
        }catch (EmptyResultDataAccessException e){
            throw new ValidationException("No user was found with the provided email");
        }

        if (assignmentRepo.isUserAlreadyAssigned(userToAdd.getId(), projectId)) {
            throw new ValidationException("This user is already assigned to the project");
        }

        try {
            assignmentRepo.addUserToProject(userToAdd.getId(), projectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("The user could not be assigned to the project due to a system error. Please try again later", ex);
        }
    }

    public void removeUserFromProject(int profileId, int projectId, User user) {

        projectService.validateProjectAccess(projectId, user);

        if (!assignmentRepo.isUserAlreadyAssigned(profileId, projectId)) {
            throw new ResourceNotFoundException("The selected user is not assigned to this project");
        }

        try {
            assignmentRepo.removeUserFromProject(profileId, projectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to remove user from project", ex);
        }
    }


}
