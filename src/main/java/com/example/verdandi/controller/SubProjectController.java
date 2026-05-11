package com.example.verdandi.controller;


import com.example.verdandi.model.SubProject;
import com.example.verdandi.service.SubProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects/{projectId}/subprojects")
public class SubProjectController {
    private final SubProjectService subProjectService;

    public SubProjectController(SubProjectService subProjectService) {
        this.subProjectService = subProjectService;
    }

    //validering af id fx negativ eller bogstaver. kan måske bruge @min
    @GetMapping("")
    public String getMySubProjects(@PathVariable int projectId,
                                   Model model) {

        List<SubProject> getSubProjects = subProjectService.getSubProjects(projectId);
        model.addAttribute("mySubProjects", getSubProjects);
        model.addAttribute("projectId", projectId);
        return "sub_projects";
    }

    @GetMapping("/create")
    public String createNewSubProject(@PathVariable int projectId, Model model) {
        SubProject subProject = new SubProject();
        subProject.setProjectId(projectId);

        model.addAttribute("subProject", subProject);
        model.addAttribute("projectId", projectId);

        return "create_sub_project";
    }

    @PostMapping("/create")
    public String saveProject(@PathVariable int projectId, @ModelAttribute SubProject subProject) {
        subProject.setProjectId(projectId);
        subProjectService.saveSubProject(subProject);

        return "redirect:/projects/" + projectId + "/subprojects";
    }

    @GetMapping("/{subprojectId}/edit")
    public String editSubProject(@PathVariable int subprojectId, @PathVariable int projectId, Model model) {
        model.addAttribute("subProject", subProjectService.findSubProjectById(subprojectId));
        model.addAttribute("projectId", projectId);
        return "edit_sub_project";
    }

    @PostMapping("/{id}/edit")
    public String updateSubProject(@PathVariable int projectId,
                                   @PathVariable int id,
                                   @ModelAttribute SubProject subProject) {
        subProject.setId(id);
        subProject.setProjectId(projectId);

        subProjectService.updateSubProject(subProject);

        return "redirect:/projects/" + projectId + "/subprojects";
    }

    @PostMapping("/{subprojectId}/delete")
    public String deleteSubProject(@PathVariable int subprojectId,
                                   @PathVariable int projectId) {

        subProjectService.deleteSubproject(subprojectId);

        return "redirect:/projects/" + projectId + "/subprojects";


    }
}