package com.example.verdandi.controller;


import com.example.verdandi.service.SubProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class SubProjectController {
    private SubProjectService subProjectService;

    public SubProjectController (SubProjectService subProjectService){
        this.subProjectService = subProjectService;
    }

    @GetMapping("/my_subprojects")
    public String getMySubProjects(Model model){
        model.addAttribute("mySubProjects", subProjectService.getSubProjects());
        return "sub_projects";
    }

}
