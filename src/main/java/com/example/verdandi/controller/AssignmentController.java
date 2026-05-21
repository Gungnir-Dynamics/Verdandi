package com.example.verdandi.controller;

import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Project;
import com.example.verdandi.model.User;
import com.example.verdandi.service.AssignmentService;
import com.example.verdandi.service.ProjectService;
import com.example.verdandi.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects/{projectId}/assignments")
public class AssignmentController {
    private final UserService userService;
    private final AssignmentService assignmentService;
    private final ProjectService projectService;

    public AssignmentController(UserService userService, AssignmentService assignmentService, ProjectService projectService) {
        this.userService = userService;
        this.assignmentService = assignmentService;
        this.projectService = projectService;
    }

    @GetMapping()
    public String getAssignmentForm(@PathVariable int projectId,
                                    Model model,
                                    HttpSession session) {

        User user = (User) session.getAttribute("user");

        Project project = projectService.getSingleProject(projectId, user);

        model.addAttribute("users", userService.getUsersForProject(projectId));
        model.addAttribute("projectId", projectId);
        model.addAttribute("project", project);

        return "project/assignment_form";
    }

    @PostMapping("/add")
    public String addUserToProject(@PathVariable int projectId, @RequestParam String email, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        try {
            assignmentService.assignUserToProject(email, projectId, user);
        } catch (ValidationException e) {
            model.addAttribute("project", projectService.getSingleProject(projectId, user));
            model.addAttribute("users", userService.getUsersForProject(projectId));
            model.addAttribute("errorMessage", e.getMessage());
            return "project/assignment_form";
        }
        return "redirect:/projects/" + projectId + "/assignments";
    }

    @PostMapping("/delete")
    public String removeUserToProject(@PathVariable int projectId, @RequestParam int userIdToDelete, HttpSession session) {
        User user = (User) session.getAttribute("user");
        assignmentService.removeUserFromProject(userIdToDelete, projectId, user);
        return "redirect:/projects/" + projectId + "/assignments";
    }
}
