package com.example.verdandi.service;

import com.example.verdandi.exception.AccessDeniedException;
import com.example.verdandi.exception.DatabaseOperationException;
import com.example.verdandi.exception.ResourceNotFoundException;
import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Project;
import com.example.verdandi.model.User;
import com.example.verdandi.repository.ProjectRepo;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepo projectRepo;

    public ProjectService(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    private void validateProjectData(Project project) {

        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new ValidationException("Project name is required");
        }

        if (project.getName().trim().length() < 3) {
            throw new ValidationException("Project name need to contain a minimum of 3 characters");
        }

        if (project.getName().trim().length() > 100) {
            throw new ValidationException("Project name can not contain more then 100 characters ");
        }


        if (project.getDeadline() != null && project.getDeadline().isBefore(LocalDate.now())) {
            throw new ValidationException("Deadline can not be before today's date");
        }


        if (project.getDescription() != null && project.getDescription().length() > 5000) {
            throw new ValidationException("Description must be a maximum of 5000 characters");
        }
    }

    public void validateProjectExists(int projectId) {
        try {
            if (!projectRepo.projectExists(projectId)) {
                throw new ResourceNotFoundException(
                        "project " + projectId + " does not exist"
                );
            }
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to retrieve data for project", ex);
        }
    }

    public Project getSingleProject(int projectId) {
        validateProjectExists(projectId);
        try {
            Project project = projectRepo.getSingleProject(projectId);
            setPriceAndEndDateForProject(project);
            return project;
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to retrieve data for project", ex);
        }

    }

    public List<Project> getProjects(int profileId, User user) {

        if (user.isAdmin()) {

            try {

                return projectRepo.getMultipleProjects();

            } catch (DataAccessException ex) {

                throw new DatabaseOperationException("Failed to retrieve data projects", ex);
            }
        } else {

            try {

                return projectRepo.getAssignedProjects(profileId);

            } catch (DataAccessException ex) {

                throw new DatabaseOperationException("Failed to retrieve data projects", ex);
            }
        }
    }

    public void saveProject(Project project) {
        validateProjectData(project);

        try {
            projectRepo.createProject(project);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to create project", ex);
        }
    }

    public void updateProject(int projectId, Project updateProject) {
        validateProjectData(updateProject);
        validateProjectExists(projectId);

        try {
            projectRepo.updateProject(projectId, updateProject);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to update project", ex);
        }

    }

    public void deleteProject(int projectId) {
        validateProjectExists(projectId);
        try {
            projectRepo.deleteProject(projectId);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Failed to delete project", ex);
        }
    }

    private void setPriceAndEndDateForProject(Project project) {
        List<User> employees = getEmployees;
        int numberOfEmployees = employees.size();
        project.setNumberOfEmployees(numberOfEmployees);
        project.setPrice(calculateProjectPrice(project, employees));
        project.setEstimatedEndDate(calculateExpectedProjectEndDate(project, numberOfEmployees));
    }

    private LocalDate calculateExpectedProjectEndDate(Project project, int numberOfEmployees) {

        int hoursPerDay = numberOfEmployees * 8;
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

    private double calculateProjectPrice(Project project, List<User> employees) {

        double hoursPerUser = (double) project.getEstimatedHours() / employees.size();
        double total = 0;

        for (User employee : employees) {
            total += employee.getHourlyRate() * hoursPerUser;
        }

        return total;
    }

    public void validateUserHasAccessToProject(int projectId, User user) {

        if (!user.isAdmin()) {
            if (!projectRepo.userHasAccessToProject(user.getId(), projectId)) {
                throw new AccessDeniedException("You do not have access to this project");
            }
        }

    }
}
