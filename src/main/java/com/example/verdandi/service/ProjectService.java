package com.example.verdandi.service;

import com.example.verdandi.exception.AccessDeniedException;
import com.example.verdandi.exception.DatabaseOperationException;
import com.example.verdandi.exception.ResourceNotFoundException;
import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Project;
import com.example.verdandi.model.User;
import com.example.verdandi.repository.AssignmentRepo;
import com.example.verdandi.repository.ProjectRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepo projectRepo;
    private final UserService userService;
    private final AssignmentRepo assignmentRepo;

    public ProjectService(ProjectRepo projectRepo, UserService userService, AssignmentRepo assignmentRepo) {
        this.projectRepo = projectRepo;
        this.userService = userService;
        this.assignmentRepo = assignmentRepo;
    }

    private void validateProjectData(Project project) {

        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new ValidationException("Project name is required");
        }

        if (project.getName().trim().length() < 3) {
            throw new ValidationException("The project name must contain at least 3 characters");
        }

        if (project.getName().trim().length() > 100) {
            throw new ValidationException("The project name may not exceed 100 characters");
        }


        if (project.getDeadline() != null && project.getDeadline().isBefore(LocalDate.now())) {
            throw new ValidationException("The deadline cannot be earlier than today");
        }


        if (project.getDescription() != null && project.getDescription().length() > 5000) {
            throw new ValidationException("Description must be a maximum of 5000 characters");
        }
    }

    public Project getSingleProject(int projectId, User user) {
        validateProjectAccess(projectId, user);
        try {
            Project project = projectRepo.getSingleProject(projectId);
            setPriceAndEndDateForProject(project);
            return project;
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("The project data could not be loaded. Please try again later", ex);
        }

    }

    public List<Project> getProjects(int profileId, User user) {

        if (user.isAdmin()) {

            try {

                List<Project> projects = projectRepo.getMultipleProjects();
                for (Project project : projects) {
                    setPriceAndEndDateForProject(project);
                }
                return projects;

            } catch (DataAccessException ex) {

                throw new DatabaseOperationException("Projects could not be loaded. Please try again later", ex);
            }
        } else {

            try {

                List<Project> projects = projectRepo.getAssignedProjects(profileId);
                for (Project project : projects) {
                    setPriceAndEndDateForProject(project);
                }
                return projects;

            } catch (DataAccessException ex) {

                throw new DatabaseOperationException("Projects could not be loaded. Please try again later", ex);
            }
        }
    }

    @Transactional
    public void saveProject(Project project, User user) {
        validateProjectData(project);

        try {
            int projectId = projectRepo.createProject(project);
            assignmentRepo.addUserToProject(user.getId(), projectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("The project could not be created due to a system error", ex);
        }
    }

    public void updateProject(int projectId, Project updateProject, User user) {
        validateProjectData(updateProject);
        validateProjectAccess(projectId, user);

        try {
            projectRepo.updateProject(projectId, updateProject);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to update project", ex);
        }

    }

    public void deleteProject(int projectId, User user) {
        validateProjectAccess(projectId, user);
        try {
            projectRepo.deleteProject(projectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to delete project", ex);
        }
    }

    private void setPriceAndEndDateForProject(Project project) {
        List<User> users = userService.getUsersForProject(project.getId());
        int numberOfEmployees = users.size();
        project.setNumberOfEmployees(numberOfEmployees);
        project.setPrice(calculateProjectPrice(project, users));
        project.setEstimatedEndDate(calculateExpectedProjectEndDate(project, numberOfEmployees));
    }

    private LocalDate calculateExpectedProjectEndDate(Project project, int numberOfUsersOnProject) {

        if (numberOfUsersOnProject == 0) {
            return project.getCreationDate();
        }

        int hoursPerDay = numberOfUsersOnProject * 8;
        int workDays = (int) Math.ceil((double) project.getEstimatedHours() / hoursPerDay);

        LocalDate date = project.getCreationDate();
        int addedDays = 0;

        while (addedDays < workDays) {
            date = date.plusDays(1);

            DayOfWeek day = date.getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                addedDays++;
            }
        }

        return date;
    }

    private double calculateProjectPrice(Project project, List<User> users) {

        if (users == null || users.isEmpty()) {
            return 0.0;
        }
        double hoursPerUser = (double) project.getEstimatedHours() / users.size();
        double total = 0;

        for (User employee : users) {
            total += employee.getHourlyRate() * hoursPerUser;
        }

        return new BigDecimal(total)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public void validateProjectExists(int projectId) {
        try {
            if (!projectRepo.projectExists(projectId)) {
                throw new ResourceNotFoundException(
                        "The project with ID " + projectId + " does not exist "
                );
            }
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("The project data could not be loaded. Please try again later", ex);
        }
    }

    public void validateUserHasAccessToProject(int projectId, User user) {

        if (user == null) {
            throw new AccessDeniedException("You do not have access to this project");
        }

        if (user.isAdmin()) {
            return;
        }

        if (!assignmentRepo.userHasAccessToProject(user.getId(), projectId)) {
            throw new AccessDeniedException("You do not have access to this project");
        }
    }

    //brug denne i stedet for validateProjectExists
    public void validateProjectAccess(int projectId, User user) {
        validateProjectExists(projectId);
        validateUserHasAccessToProject(projectId, user);
    }
}
