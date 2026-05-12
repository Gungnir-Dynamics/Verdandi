package com.example.verdandi.controller;


import com.example.verdandi.exception.ValidationException;
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
        return "subproject/sub_projects";
    }

    @GetMapping("/create")
    public String createNewSubProject(@PathVariable int projectId,
                                      Model model) {

        model.addAttribute("subproject", new SubProject());
        model.addAttribute("projectId", projectId);

        return "subproject/create_sub_project";
    }

    @PostMapping("/create")
    public String saveSubProject(@PathVariable int projectId,
                                 @ModelAttribute SubProject subProject,
                              Model model) {

        try {

            subProjectService.saveSubProject(subProject, projectId);

            return "redirect:/projects/" + projectId + "/subprojects";

        } catch (ValidationException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("subproject", subProject);
            model.addAttribute("projectId", projectId);

            return "subproject/create_sub_project";
        }
    }

    @GetMapping("/{subprojectId}/edit")
    public String editSubProject(@PathVariable int subprojectId,
                                 @PathVariable int projectId,
                                 Model model) {

        subProjectService.validateSubProjectBelongsToProject(projectId, subprojectId);

        model.addAttribute("subproject", subProjectService.findSubProjectById(projectId, subprojectId));

        model.addAttribute("projectId", projectId);

        return "subproject/edit_sub_project";
    }

    @PostMapping("/{subprojectId}/edit")
    public String updateSubProject(@PathVariable int projectId,
                                   @PathVariable int subprojectId,
                                   @ModelAttribute SubProject subProject,
                                   Model model) {

        try {

            subProject.setId(subprojectId);
            subProject.setProjectId(projectId);
            subProjectService.updateSubProject(projectId, subprojectId, subProject);

            return "redirect:/projects/" + projectId + "/subprojects";

        } catch (ValidationException ex) {

            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("subproject", subProject);

            model.addAttribute("projectId", projectId);
            model.addAttribute("subprojectId", subprojectId);
        }
        return "subproject/edit_sub_project";

    }

    @PostMapping("/{subprojectId}/delete")
    public String deleteSubProject(@PathVariable int subprojectId,
                                   @PathVariable int projectId) {

        subProjectService.deleteSubproject(projectId, subprojectId);

        return "redirect:/projects/" + projectId + "/subprojects";


    }
}