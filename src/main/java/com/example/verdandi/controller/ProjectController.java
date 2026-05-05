package com.example.verdandi.controller;


import com.example.verdandi.service.ProjectService;
import jakarta.servlet.ServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ProjectController {
    private ProjectService projectService;


    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping("/project")
    public String getMyProjects(Model model){
        model.addAttribute("myProjects", projectService.getProjects());
return "projects";
    }


}
