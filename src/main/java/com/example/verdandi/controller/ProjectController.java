package com.example.verdandi.controller;


import com.example.verdandi.model.Project;
import com.example.verdandi.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/create_project")
    public String createNewProject(Model model){
        model.addAttribute("project", new Project());
        return "/create_project";
    }

    @PostMapping("/create_project")
    public String saveProject(@ModelAttribute Project project){
        projectService.saveProject(project);
        return "redirect:/projects";
    }

}
