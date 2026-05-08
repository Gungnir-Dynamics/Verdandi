package com.example.verdandi.controller;

import com.example.verdandi.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects/{projectId}/subprojects/{subprojectId}/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping("")
    public String showTasks (@PathVariable int subprojectId,
                             @PathVariable int projectId,
                             Model model){
        model.addAttribute("tasks", taskService.getTasksBySubproject(projectId, subprojectId));
        return "tasks";
    }
}
