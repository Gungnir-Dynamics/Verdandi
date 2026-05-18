package com.example.verdandi.controller;


import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Project;
import com.example.verdandi.model.User;
import com.example.verdandi.repository.ProjectRepo;
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

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping("")
    public String getMyProjects(Model model){
        model.addAttribute("myProjects", projectService.getMultipleProjects());
        return "/project/projects";
    }


    // Timothy's Tilføjelse -
    // Virkede ikke i UserController da URL ikke passer
    @GetMapping("/my_projects")
    public String getAssignedProjects(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("myProjects", projectService.getAssignedProjects(user.getId()));
        return "/project/my_projects";

    }

    @GetMapping("/create")
    public String showCreationForm(Model model){
        if (!model.containsAttribute("project")) {
            model.addAttribute("project", new Project());
        }

        return "/project/create_project";
    }

    @PostMapping("/create")
    public String saveProject(@ModelAttribute Project project, RedirectAttributes redirectAttributes, Model model){

        try {
            projectService.saveProject(project);
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
                               Model model) {

        Project project = projectService.getSingleProject(projectId);

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
                                Model model) {

        try {
            projectService.updateProject(projectId, project);
            redirectAttributes.addFlashAttribute("successMessage", "Project was updated successfully");
            return "redirect:/projects";

        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("project", project);

            return "project/edit_project";
        }
    }



    @PostMapping("{projectId}/delete")
    public String deleteProject (@PathVariable int projectId, RedirectAttributes redirectAttributes) {
        try {
            projectService.deleteProject(projectId);
            redirectAttributes.addFlashAttribute("successMessage", "Project was deleted successfully");
            return "redirect:/projects";
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/projects";
    }
}