package com.example.verdandi.controller;


import com.example.verdandi.model.Project;
import com.example.verdandi.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
        return "projects";
    }

    //ændre url til create
    //Manglende validering fx (tomt navn, for lang tekst, deadline før creationDate, negative tal)
    @GetMapping("/create_project")
    public String createNewProject(Model model){
        model.addAttribute("project", new Project());
        return "/create_project";
    }

    @PostMapping("/create_project")
    public String saveProject(@ModelAttribute Project project, RedirectAttributes redirectAttributes){

        try {
            projectService.saveProject(project);
            redirectAttributes.addFlashAttribute("successMessage", "Projektet blev oprettet succesfuldt!");
            return "redirect:/projects";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/create_project";
        }
    }

}
