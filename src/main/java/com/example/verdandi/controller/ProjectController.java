package com.example.verdandi.controller;


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
    public String createNewProject(Model model){
        model.addAttribute("project", new Project());
        return "/project/create_project";
    }

    @PostMapping("/create")
    public String saveProject(@ModelAttribute Project project, BindingResult bindingResult, RedirectAttributes redirectAttributes){
//
//        if (bindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Ugyldig datoformat eller manglende felter");
//            redirectAttributes.addFlashAttribute("project", project);
//            return "redirect:/projects/create";
//        }
        try {
            projectService.saveProject(project);

            redirectAttributes.addFlashAttribute("successMessage", "the project '" + project.getName() + "' was  created successfully ");
            return "redirect:/projects";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("project", project);
            return "redirect:/projects/create";
        }
    }

//    @GetMapping
//    public String editProject(){
//
//    }

    @PostMapping("{projectId}/delete")
    public String deleteProject (@PathVariable int projectId){
        projectService.deleteProject(projectId);
        return "redirect:/projects";
    }


}
