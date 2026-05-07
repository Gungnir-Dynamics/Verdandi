package com.example.verdandi.controller;


import com.example.verdandi.model.Project;
import com.example.verdandi.service.ProjectService;
import jakarta.servlet.ServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        List<Project> getProjects = projectService.getProjects();
        model.addAttribute("myProjects", getProjects);
        return "projects";
    }
}