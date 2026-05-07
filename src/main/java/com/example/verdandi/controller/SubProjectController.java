package com.example.verdandi.controller;


import com.example.verdandi.model.SubProject;
import com.example.verdandi.service.SubProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/projects/{projectId}/subprojects")
public class SubProjectController {
    private final SubProjectService subProjectService;

    public SubProjectController(SubProjectService subProjectService) {
        this.subProjectService = subProjectService;
    }

    @GetMapping("")
    public String getMySubProjects(
            @PathVariable int projectId,
            Model model) {

        List<SubProject> getSubProjects = subProjectService.getSubProjects(projectId);
        model.addAttribute("mySubProjects", getSubProjects);
        return "sub_projects";
    }

    @GetMapping("/subproject/{id}")
    public String getSubProject(@PathVariable int id, Model model) {
        SubProject subProject = subProjectService.getSubProjectById(id);
        model.addAttribute("subProject", subProject);
        return "sub_projects";
    }
}
