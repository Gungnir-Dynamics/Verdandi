package com.example.verdandi.controller;


import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Project;
import com.example.verdandi.model.User;
import com.example.verdandi.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService ){
        this.projectService = projectService;
    }

    @GetMapping("")
    public String getProjects(Model model, HttpSession session){

        User user = (User) session.getAttribute("user");

        model.addAttribute("myProjects", projectService.getProjects(user.getId(), user));
        return "/project/projects";
    }


    @GetMapping("/create")
    public String showCreationForm(Model model){
        if (!model.containsAttribute("project")) {
            model.addAttribute("project", new Project());
        }

        return "/project/create_project";
    }

    @PostMapping("/create")
    public String saveProject(@ModelAttribute Project project, RedirectAttributes redirectAttributes, Model model, HttpSession session){

        User user = (User) session.getAttribute("user");
        try {
            projectService.saveProject(project, user);
            redirectAttributes.addFlashAttribute("successMessage", "Project was created successfully");
            return "redirect:/projects";

        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("project", project);
            return "/project/create_project";
        }
    }

    @GetMapping("/{projectId}/edit")
    public String showEditForm(@PathVariable int projectId,
                               Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        Project project = projectService.getSingleProject(projectId, user);

        if (!model.containsAttribute("project")) {
            model.addAttribute("project", project);
        }
        model.addAttribute("projectId", projectId);
             return "project/edit_project";
    }

    @PostMapping("/{projectId}/edit")
    public String updateProject(@PathVariable int projectId,
                             @ModelAttribute Project project,
                             RedirectAttributes redirectAttributes,
                                Model model,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        try {
            projectService.updateProject(projectId, project, user);
            redirectAttributes.addFlashAttribute("successMessage", "Project was updated successfully");
            return "redirect:/projects";

        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("project", project);

            return "project/edit_project";
        }
    }

    @PostMapping("{projectId}/delete")
    public String deleteProject (@PathVariable int projectId, RedirectAttributes redirectAttributes, HttpSession session) {
        User user = (User) session.getAttribute("user");
        try {
            projectService.deleteProject(projectId, user);
            redirectAttributes.addFlashAttribute("successMessage", "Project was deleted successfully");
            return "redirect:/projects";
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/projects";
    }
}