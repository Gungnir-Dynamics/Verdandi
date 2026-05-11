package com.example.verdandi.controller;


import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Project;
import com.example.verdandi.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @GetMapping("/create")
    public String showCreationForm(Model model){
        if (!model.containsAttribute("project")) {
            model.addAttribute("project", new Project());
        }

        return "/project/create_project";
    }

    @PostMapping("/create")
    public String saveProject(@ModelAttribute Project project, RedirectAttributes redirectAttributes){

        try {
            projectService.saveProject(project);
            redirectAttributes.addFlashAttribute("successMessage", "Project was created successfully");
            return "redirect:/projects";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("project", project);
            return "redirect:/projects/create";
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
                             RedirectAttributes redirectAttributes) {

        try {
            projectService.updateProject(projectId, project);
            redirectAttributes.addFlashAttribute("successMessage", "Project was updated successfully");
            return "redirect:/projects";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("project", project);

            return "redirect:/projects/" + projectId + "/edit";
        }
    }



    @PostMapping("{projectId}/delete")
    public String deleteProject (@PathVariable int projectId){
        projectService.deleteProject(projectId);
        return "redirect:/projects";
    }


}
